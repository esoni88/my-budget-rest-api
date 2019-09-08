package it.italiancoders.mybudgetrest.dao.movement.impl;

import it.italiancoders.mybudgetrest.dao.movement.MovementDaoCriteria;
import it.italiancoders.mybudgetrest.model.dto.User;
import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import it.italiancoders.mybudgetrest.model.entity.MovementEntity;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import it.italiancoders.mybudgetrest.model.internal.CategoryEntityExpenseSummary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MovementDaoCriteriaImpl implements MovementDaoCriteria {
    @Autowired
    EntityManager em;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryEntityExpenseSummary> calculateCategoryEntityExpenseSummary
            (Authentication token, Integer day, Integer month, Integer year, Integer week, CategoryEntity categoryEntity) {
        User currentUser = (User) token.getPrincipal();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> allPredicates = new ArrayList<>();
        UserEntity userEntity = modelMapper.map(currentUser, UserEntity.class);
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();


        Root<MovementEntity> rootMovement = cq.from(MovementEntity.class);
        Join<MovementEntity, CategoryEntity> typeJoin = rootMovement.join("category");
        cq.multiselect(typeJoin, cb.sum(rootMovement.get("amount")));

        allPredicates.add(cb.equal(rootMovement.get("user"), userEntity));
        if (day != null) {
            allPredicates.add(cb.equal(rootMovement.get("day"), day));
        } else if (week != null) {
            allPredicates.add(cb.equal(rootMovement.get("week"), week));

        }
        if (month != null) {
            allPredicates.add(cb.equal(rootMovement.get("month"), month));
        }
        if (year != null) {
            allPredicates.add(cb.equal(rootMovement.get("year"), year));
        }
        if (categoryEntity != null) {
            allPredicates.add(cb.equal(rootMovement.get("category"), categoryEntity));
        }

        cq.where(allPredicates.toArray(new Predicate[0]));
        cq.groupBy(typeJoin);

        List<Tuple> tuples = em.createQuery(cq).getResultList();
        return tuples.stream().map((v) ->
                CategoryEntityExpenseSummary
                .newBuilder()
                .category((CategoryEntity) v.get(0))
                .totalAmount((Double) v.get(1))
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public Page<MovementEntity> find(Authentication token, Integer day, Integer month, Integer year, Integer week, CategoryEntity categoryEntity, Pageable pageable) {
        User currentUser = (User) token.getPrincipal();
        UserEntity userEntity = modelMapper.map(currentUser, UserEntity.class);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MovementEntity> cq = cb.createQuery(MovementEntity.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<MovementEntity> rootCount = countQuery.from(MovementEntity.class);
        Root<MovementEntity> root = cq.from(MovementEntity.class);

        countQuery.select(cb.count(rootCount));

        List<Predicate> allPredicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        allPredicates.add(cb.equal(root.get("user"), userEntity));
        countPredicates.add(cb.equal(rootCount.get("user"), userEntity));

        if (day != null) {
            allPredicates.add(cb.equal(root.get("day"), day));
            countPredicates.add(cb.equal(rootCount.get("day"), day));
        } else {
            if (week != null) {
                allPredicates.add(cb.equal(root.get("week"), week));
                countPredicates.add(cb.equal(rootCount.get("week"), week));
            }
        }
        if (month != null) {
            allPredicates.add(cb.equal(root.get("month"), month));
            countPredicates.add(cb.equal(rootCount.get("month"), month));

        }
        if (year != null) {
            allPredicates.add(cb.equal(root.get("year"), year));
            countPredicates.add(cb.equal(rootCount.get("year"), year));
        }
        if (categoryEntity != null) {
            allPredicates.add(cb.equal(root.get("category"), categoryEntity));
            countPredicates.add(cb.equal(rootCount.get("category"), categoryEntity));
        }
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long count = em.createQuery(countQuery).getSingleResult();

        if (count == 0) {
            return  new PageImpl<>(new ArrayList<>(), pageable, count);
        }

        cq.where(allPredicates.toArray(new Predicate[0]));
        cq.orderBy(em.getCriteriaBuilder().desc(root.get("executedAt")));

        TypedQuery<MovementEntity> query = em.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        List<MovementEntity> retval = query.getResultList();
        return  new PageImpl<>(retval, pageable, count);
    }
}
