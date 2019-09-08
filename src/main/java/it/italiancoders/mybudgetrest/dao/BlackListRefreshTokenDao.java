package it.italiancoders.mybudgetrest.dao;

import it.italiancoders.mybudgetrest.model.entity.BlackListRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRefreshTokenDao extends JpaRepository<BlackListRefreshTokenEntity, String> {
    BlackListRefreshTokenEntity findByRefreshToken(String refreshToken);
}
