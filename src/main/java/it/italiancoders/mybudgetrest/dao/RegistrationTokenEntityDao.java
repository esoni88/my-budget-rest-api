package it.italiancoders.mybudgetrest.dao;

import it.italiancoders.mybudgetrest.model.entity.RegistrationTokenEntity;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationTokenEntityDao extends JpaRepository<RegistrationTokenEntity, String> {
    Optional<RegistrationTokenEntity> findOneByToken(String token);
    Optional<RegistrationTokenEntity> findOneByUsernameIgnoreCase(String token);

}
