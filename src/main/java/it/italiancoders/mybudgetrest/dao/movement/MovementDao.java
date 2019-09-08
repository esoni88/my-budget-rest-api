package it.italiancoders.mybudgetrest.dao.movement;

import it.italiancoders.mybudgetrest.model.entity.MovementEntity;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovementDao  extends JpaRepository<MovementEntity, Long> {
    Optional<MovementEntity> findOneByUserAndId(UserEntity username, Long id);
}
