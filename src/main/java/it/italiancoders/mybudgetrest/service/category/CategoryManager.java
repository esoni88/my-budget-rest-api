package it.italiancoders.mybudgetrest.service.category;

import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.model.dto.Category;
import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import it.italiancoders.mybudgetrest.service.category.impl.CategoryManagerImpl;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface CategoryManager {
    List<Category> findAll(Authentication token);
    CategoryEntity toCategoryEntity(Category c);
    Category toCategory(CategoryEntity c);
    Optional<Category> findByName(Authentication token, String name);
    Category insertUserCategory(Authentication token, Category category);
    Optional<Category> findById(Authentication token, Long id);
    void deleteById(Authentication token, Long id) throws NoSuchEntityException, CategoryManagerImpl.CategoryReadonlyException;
    void update(Authentication token,Category newValue);
}
