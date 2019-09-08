package it.italiancoders.mybudgetrest.service.movement.impl;

import it.italiancoders.mybudgetrest.dao.movement.MovementDao;
import it.italiancoders.mybudgetrest.dao.movement.MovementDaoCriteria;
import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.model.dto.*;
import it.italiancoders.mybudgetrest.model.entity.MovementEntity;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import it.italiancoders.mybudgetrest.model.internal.CategoryEntityExpenseSummary;
import it.italiancoders.mybudgetrest.service.category.CategoryManager;
import it.italiancoders.mybudgetrest.service.movement.MovementManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovementManagerImpl implements MovementManager {

    @Autowired
    MovementDaoCriteria movementDaoCriteria;

    @Autowired
    MovementDao movementDao;

    @Autowired
    CategoryManager categoryManager;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Optional<Movement> findById(Authentication token, Long id) {
        User currentUser = (User) token.getPrincipal();
        UserEntity userEntity = modelMapper.map(currentUser, UserEntity.class);
        Optional<MovementEntity> retval = movementDao.findOneByUserAndId(userEntity, id);
        Movement movement = retval.map(this::toMovement).orElse(null);
        return Optional.ofNullable(movement);
    }

    @Override
    public void delete(Authentication token, Long id) throws NoSuchEntityException {
        User currentUser = (User) token.getPrincipal();
        UserEntity userEntity = modelMapper.map(currentUser, UserEntity.class);
        MovementEntity movementEntity = movementDao.findOneByUserAndId(userEntity, id)
                .orElseThrow(NoSuchEntityException::new);
        movementDao.delete(movementEntity);
    }

    @Override
    public void update(Authentication token, Movement newValue) throws NoSuchEntityException {
        User currentUser = (User) token.getPrincipal();
        UserEntity userEntity = modelMapper.map(currentUser, UserEntity.class);
        movementDao.findOneByUserAndId(userEntity, newValue.getId())
                .orElseThrow(NoSuchEntityException::new);

        movementDao.save(toMovementEntity(token, newValue));
    }

    @Override
    public void insert(Authentication token, Movement m) {
        MovementEntity movementEntity = toMovementEntity(token, m);
        movementDao.save(movementEntity);
    }

    @Override
    public Movement toMovement(MovementEntity movementEntity) {
        if (movementEntity == null) {
            return null;
        } else {
            return modelMapper.map(movementEntity, Movement.class);
        }
    }

    @Override
    public MovementEntity toMovementEntity(Authentication token, Movement movement) {
        MovementEntity newValueEntity =  modelMapper.map(movement, MovementEntity.class);
        if (token != null) {
            User currentUser = (User) token.getPrincipal();
            UserEntity userEntity = modelMapper.map(currentUser, UserEntity.class);
            newValueEntity.setUser(userEntity);
        }
        if (newValueEntity.getExecutedAt() == null) {
            newValueEntity.setExecutedAt(OffsetDateTime.now());
        }
        newValueEntity.setDay(newValueEntity.getExecutedAt().getDayOfMonth());
        newValueEntity.setMonth(newValueEntity.getExecutedAt().getMonthValue());
        newValueEntity.setYear(newValueEntity.getExecutedAt().getYear());

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = newValueEntity.getExecutedAt().get(weekFields.weekOfMonth());
        newValueEntity.setWeek(weekNumber);

        return newValueEntity;
    }

    @Override
    public MovementListPage find(Authentication token, Integer day, Integer month, Integer year, Integer week, Category category, Pageable pageable) {
        Page<MovementEntity> pageMovEntity = movementDaoCriteria.find(token,
                        day,
                        month,
                        year,
                        week,
                        category == null ? null : categoryManager.toCategoryEntity(category),
                        pageable);

        MovementListPage retval = modelMapper.map(pageMovEntity, MovementListPage.class);
        retval.setContents(pageMovEntity.getContent().stream().map(this::toMovement).collect(Collectors.toList()));
        return retval;
    }

    @Override
    public ExpenseSummary calculateExpenseSummary(Authentication token, Integer day, Integer month, Integer year, Integer week, Category category, Pageable pageable) {
        List<CategoryEntityExpenseSummary> recap = movementDaoCriteria.calculateCategoryEntityExpenseSummary(
                token,
                day,
                month,
                year,
                week,
                category == null ? null : categoryManager.toCategoryEntity(category));

        List<CategoryMovementOverview> categoryMovementOverviews =
                recap.stream().map((v) -> CategoryMovementOverview
                        .newBuilder()
                        .category(categoryManager.toCategory(v.getCategory()))
                        .totalAmount(v.getTotalAmount())
                        .build()
                ).collect(Collectors.toList());

        Double total = categoryMovementOverviews.stream()
                .reduce(0.0d, (subtotal, element) -> subtotal + element.getTotalAmount(), Double::sum);

        return ExpenseSummary.newBuilder()
                .lastMovements(find(token, day, month, year, week, category, pageable))
                .totalAmount(total)
                .categoryOverview(categoryMovementOverviews)
                .build();
    }
}
