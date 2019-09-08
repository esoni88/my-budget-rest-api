package it.italiancoders.mybudgetrest.service.user;

import it.italiancoders.mybudgetrest.exception.security.ExpiredTokenException;
import it.italiancoders.mybudgetrest.exception.security.MailNotSentException;
import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.exception.security.UserNotActiveException;
import it.italiancoders.mybudgetrest.model.dto.User;
import it.italiancoders.mybudgetrest.model.dto.UserRegistrationInfo;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;

import java.util.Optional;

public interface UserManager {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    void registrateUser(UserRegistrationInfo registrationInfo) throws MailNotSentException;
    void validateUser(String username) throws UserNotActiveException;
    void confirmRegistration(String registrationToken) throws NoSuchEntityException, ExpiredTokenException;

    void resendConfirmRegistrationMail(String username, String email);
}
