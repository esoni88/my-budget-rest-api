package it.italiancoders.mybudgetrest.dao;

import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryDao extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUsernameInOrderByName(List<String> usernames);
    Optional<CategoryEntity> findOneByUsernameInAndName(List<String> usernames, String name);
    Optional<CategoryEntity> findOneByUsernameInAndId(List<String> usernames, Long id);

}
