package it.italiancoders.mybudgetrest.dao;

import it.italiancoders.mybudgetrest.model.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRoleEntity, String> {
}
