package it.italiancoders.mybudgetrest.service.movement;

import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.model.dto.Category;
import it.italiancoders.mybudgetrest.model.dto.ExpenseSummary;
import it.italiancoders.mybudgetrest.model.dto.Movement;
import it.italiancoders.mybudgetrest.model.dto.MovementListPage;
import it.italiancoders.mybudgetrest.model.entity.MovementEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Optional;


public interface MovementManager {
    Optional<Movement> findById(Authentication token, Long id);
    void delete(Authentication token, Long id) throws NoSuchEntityException;
    void update(Authentication token, Movement newValue) throws NoSuchEntityException;
    void insert(Authentication token, Movement m);
    Movement toMovement(MovementEntity movementEntity);
    MovementEntity toMovementEntity(Authentication token, Movement movement);
    MovementListPage find(Authentication token, Integer day, Integer month, Integer year, Integer week, Category category, Pageable pageable);
    ExpenseSummary calculateExpenseSummary(Authentication token, Integer day, Integer month, Integer year, Integer week, Category category, Pageable pageable);
}
