package it.italiancoders.mybudgetrest.controller;

import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.model.dto.*;
import it.italiancoders.mybudgetrest.service.category.CategoryManager;
import it.italiancoders.mybudgetrest.service.local.LocaleUtilsMessage;
import it.italiancoders.mybudgetrest.service.movement.MovementManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
public class MovementController {

    @Autowired
    LocaleUtilsMessage localeUtilsMessage;

    @Autowired
    MovementManager movementManager;

    @Autowired
    CategoryManager categoryManager;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/v1/movements", method  = RequestMethod.GET)
    public ResponseEntity<?> getCategories(Authentication token, Pageable p,
                                           @RequestParam(value = "year", required = true) final Integer year,
                                           @RequestParam(value = "month", required = true) final Integer month,
                                           @RequestParam(value = "day", required = false) final Integer day,
                                           @RequestParam(value = "week", required = false) final Integer week,
                                           @RequestParam(value = "category", required = false) final Long categoryId
                                           ) throws AuthenticationException {
        Category category = null;
        if (categoryId != null) {
            category = categoryManager.findById(token, categoryId).orElse(null);
            if (category == null) {
                ResponseEntity.ok(modelMapper.map(new PageImpl<>(new ArrayList<>(), p, 0), MovementListPage.class));
            }
        }
        MovementListPage listPage = movementManager.find(token, day, month, year, week, category, p);
        return ResponseEntity.ok(listPage);
    }

    @RequestMapping(value = "/v1/movements/{id}", method  = RequestMethod.GET)
    public ResponseEntity<?> getCategoryByiD(Authentication token,
                                             @PathVariable Long id) throws AuthenticationException {
        Movement movement = movementManager.findById(token, id).orElseThrow(NoSuchEntityException::new);
        return ResponseEntity.ok(movement);
    }

    @RequestMapping(value = "/v1/movements", method  = RequestMethod.POST)
    public ResponseEntity<?> insMovement(Authentication token,
                                         @RequestBody @Valid Movement newMovement) throws AuthenticationException {
        movementManager.insert(token, newMovement);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/v1/movements/{id}", method  = RequestMethod.PUT)
    public ResponseEntity<?> updMovement(Authentication token,
                                         @PathVariable Long id,
                                         @RequestBody Movement newMovement) throws AuthenticationException {
        newMovement.setId(id);
        movementManager.update(token, newMovement);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/v1/movements/{id}", method  = RequestMethod.DELETE)
    public ResponseEntity<?> delMovement(Authentication token, @PathVariable Long id) throws AuthenticationException {
        movementManager.findById(token, id).orElseThrow(NoSuchEntityException::new);
        movementManager.delete(token, id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/v1/expense-summary", method  = RequestMethod.GET)
    public ResponseEntity<?> getExpenseSummary(Pageable p,
                                               Authentication token,
                                               @RequestParam(value = "year", required = true) final Integer year,
                                               @RequestParam(value = "month", required = true) final Integer month,
                                               @RequestParam(value = "day", required = false) final Integer day,
                                               @RequestParam(value = "week", required = false) final Integer week

    ) throws AuthenticationException {
        ExpenseSummary retval = movementManager.calculateExpenseSummary(token, day, month, year, week,null, p);
        return ResponseEntity.ok(retval);
    }
}
