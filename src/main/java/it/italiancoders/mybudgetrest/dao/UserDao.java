package it.italiancoders.mybudgetrest.dao;

import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<UserEntity, String> {
    UserEntity findByUsernameIgnoreCase(String username);
    Optional<UserEntity> findOneByEmailIgnoreCase(String email);

}
