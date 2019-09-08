package it.italiancoders.mybudgetrest.service.category.impl;

import it.italiancoders.mybudgetrest.dao.CategoryDao;
import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.model.dto.Category;
import it.italiancoders.mybudgetrest.model.dto.User;
import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import it.italiancoders.mybudgetrest.service.category.CategoryManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CategoryManagerImpl  implements CategoryManager {

    public class CategoryReadonlyException extends RuntimeException {
        public CategoryReadonlyException(String s) {
            super(s);
        }

    }

    public static final String GLOBAL_USERNAME = "*";

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Category> findAll(Authentication token) {
        User currentUser = (User) token.getPrincipal();
        List<String> usernames = Arrays.asList(GLOBAL_USERNAME, currentUser.getUsername());
        List<CategoryEntity> categoryEntities = categoryDao.findByUsernameInOrderByName(usernames);
        if (categoryEntities == null) {
            return new ArrayList<>();
        }

        return categoryEntities.stream()
                .map((v) -> toCategory(v))
                .collect(Collectors.toList());

    }

    @Override
    public CategoryEntity toCategoryEntity(Category c) {
        CategoryEntity categoryEntity = modelMapper.map(c, CategoryEntity.class);
        return categoryEntity;
    }

    @Override
    public Category toCategory(CategoryEntity c) {
        return modelMapper.map(c, Category.class);
    }

    @Override
    public Optional<Category> findByName(Authentication token, String name) {
        User currentUser = (User) token.getPrincipal();
        List<String> usernames = Arrays.asList(GLOBAL_USERNAME, currentUser.getUsername());
        return categoryDao.findOneByUsernameInAndName(usernames, name).map(this::toCategory);
    }

    @Override
    public Category insertUserCategory(Authentication token, Category category) {
        User currentUser = (User) token.getPrincipal();
        CategoryEntity categoryEntity = toCategoryEntity(category);
        categoryEntity.setId(null);
        categoryEntity.setIsReadOnly(false);
        categoryEntity.setUsername(currentUser.getUsername());

        categoryEntity = categoryDao.save(categoryEntity);

        return  modelMapper.map(categoryEntity, Category.class);
    }

    @Override
    public Optional<Category> findById(Authentication token, Long id) {
        User currentUser = (User) token.getPrincipal();
        List<String> usernames = Arrays.asList(GLOBAL_USERNAME, currentUser.getUsername());
        return categoryDao.findOneByUsernameInAndId(usernames, id).map(this::toCategory);
    }

    @Override
    public void deleteById(Authentication token, Long id) throws NoSuchEntityException, CategoryReadonlyException {
        User currentUser = (User) token.getPrincipal();
        List<String> usernames = Arrays.asList(GLOBAL_USERNAME, currentUser.getUsername());
        CategoryEntity categoryEntity = categoryDao.findOneByUsernameInAndId(usernames, id).orElseThrow(NoSuchEntityException::new);
        if (categoryEntity.getIsReadOnly()) {
            throw new CategoryReadonlyException(categoryEntity.getName());
        }
        categoryDao.delete(categoryEntity);
    }

    @Override
    public void update(Authentication token,Category newValue) {
        User currentUser = (User) token.getPrincipal();
        List<String> usernames = Arrays.asList(GLOBAL_USERNAME, currentUser.getUsername());
        CategoryEntity categoryEntity = categoryDao.findOneByUsernameInAndId(usernames, newValue.getId())
                .orElseThrow(NoSuchEntityException::new);
        CategoryEntity newValueEntity =  toCategoryEntity(newValue);
        newValueEntity.setUsername(categoryEntity.getUsername());
        newValueEntity.setIsReadOnly(categoryEntity.getIsReadOnly());
        categoryDao.save(newValueEntity);

    }
}
