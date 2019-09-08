package it.italiancoders.mybudgetrest.dao.movement;

import it.italiancoders.mybudgetrest.model.dto.Movement;
import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import it.italiancoders.mybudgetrest.model.entity.MovementEntity;
import it.italiancoders.mybudgetrest.model.internal.CategoryEntityExpenseSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MovementDaoCriteria {
   Page<MovementEntity> find(Authentication token, Integer day, Integer month, Integer year,Integer week, CategoryEntity categoryEntity, Pageable pageable);
   public List<CategoryEntityExpenseSummary> calculateCategoryEntityExpenseSummary
           (Authentication token, Integer day, Integer month, Integer year, Integer week, CategoryEntity categoryEntity);
}
