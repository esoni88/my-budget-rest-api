package it.italiancoders.mybudgetrest.controller;

import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.exception.security.RestException;
import it.italiancoders.mybudgetrest.model.dto.Category;
import it.italiancoders.mybudgetrest.service.category.CategoryManager;
import it.italiancoders.mybudgetrest.service.category.impl.CategoryManagerImpl;
import it.italiancoders.mybudgetrest.service.local.LocaleUtilsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class CategoryController {

    @Autowired
    LocaleUtilsMessage localeUtilsMessage;

    @Autowired
    CategoryManager categoryManager;
    @RequestMapping(value = "/v1/categories", method  = RequestMethod.GET)
    public ResponseEntity<?> getCategories(HttpServletResponse response, Authentication token) throws AuthenticationException {
        return ResponseEntity.ok(categoryManager.findAll(token));
    }

    @RequestMapping(value = "/v1/categories/{id}", method  = RequestMethod.GET)
    public ResponseEntity<?> getCategoryById(HttpServletResponse response, @PathVariable Long id,  Authentication token) throws AuthenticationException {
        Category category = categoryManager.findById(token, id).orElseThrow(NoSuchEntityException::new);
        return ResponseEntity.ok(category);
    }

    @RequestMapping(value = "/v1/categories/{id}", method  = RequestMethod.DELETE)
    public ResponseEntity<?> categoryDelete(HttpServletResponse response, @PathVariable Long id,  Authentication token) throws AuthenticationException {
        try {
            categoryManager.deleteById(token, id);
        } catch (CategoryManagerImpl.CategoryReadonlyException c) {
            throw RestException.newBuilder()
                    .title(localeUtilsMessage.getErrorLocalizedMessage("CategoryController.categoryDelete.failed.title", null))
                    .detail(localeUtilsMessage.getErrorLocalizedMessage("CategoryController.categoryDelete.categoryIsReadonly", new Object[]{c.getMessage()}))
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/v1/categories/{id}", method  = RequestMethod.PUT)
    public ResponseEntity<?> categoryPut(HttpServletResponse response, @PathVariable Long id,  Authentication token, @RequestBody @Valid final Category category) throws AuthenticationException {
        categoryManager.findById(token, id).orElseThrow(NoSuchEntityException::new);
        Category existingCategoryWithSameName = categoryManager.findByName(token, category.getName()).orElse(null);

        if (existingCategoryWithSameName != null && !existingCategoryWithSameName.getId().equals(id)) {
            throw RestException.newBuilder()
                    .title(localeUtilsMessage.getErrorLocalizedMessage("CategoryController.categoryPut.failed.title", null))
                    .detail(localeUtilsMessage.getErrorLocalizedMessage("CategoryController.categoryPut.categoryAlreadyExist", new Object[]{category.getName()}))
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        }
        categoryManager.update(token, category);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/v1/categories", method = RequestMethod.POST, produces = {MediaType
            .APPLICATION_JSON_VALUE})
    public ResponseEntity<?> categoryPost(HttpServletResponse response, Authentication token,  @RequestBody @Valid final Category category) throws AuthenticationException {
        Category existingCategory = categoryManager.findByName(token, category.getName()).orElse(null);
        if (existingCategory != null) {
            throw RestException.newBuilder()
                    .title(localeUtilsMessage.getErrorLocalizedMessage("CategoryController.categoryPost.failed.title", null))
                    .detail(localeUtilsMessage.getErrorLocalizedMessage("CategoryController.categoryPost.categoryAlreadyExist", new Object[]{category.getName()}))
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        }

        Category retval = categoryManager.insertUserCategory(token, category);

        return new ResponseEntity<>(retval, HttpStatus.CREATED);
    }
}
