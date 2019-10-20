package it.italiancoders.mybudgetrest.service.user.impl;

import it.italiancoders.mybudgetrest.MybudgetRestApplication;
import it.italiancoders.mybudgetrest.dao.RegistrationTokenEntityDao;
import it.italiancoders.mybudgetrest.dao.UserDao;
import it.italiancoders.mybudgetrest.exception.security.ExpiredTokenException;
import it.italiancoders.mybudgetrest.exception.security.MailNotSentException;
import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.exception.security.UserNotActiveException;
import it.italiancoders.mybudgetrest.model.dto.User;
import it.italiancoders.mybudgetrest.model.dto.UserRegistrationInfo;
import it.italiancoders.mybudgetrest.model.dto.UserRole;
import it.italiancoders.mybudgetrest.model.entity.RegistrationTokenEntity;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import it.italiancoders.mybudgetrest.model.entity.UserRoleEntity;
import it.italiancoders.mybudgetrest.service.local.LocaleUtilsMessage;
import it.italiancoders.mybudgetrest.service.user.UserManager;
import it.italiancoders.mybudgetrest.utils.CommonUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserManagerImpl implements UserManager {
    protected static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RegistrationTokenEntityDao registrationTokenEntityDao;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Value("${registration.expiration}")
    private Integer registrationTokenExpiration;

    @Value("${registration.confirmUrl}")
    private String confirmUrl;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    LocaleUtilsMessage localeUtilsMessage;

    User toUser(UserEntity userEntity) {
        User user = modelMapper.map(userEntity, User.class);
        if (userEntity.getRoles() != null) {
            user.setRoles(
                    userEntity.getRoles()
                            .stream()
                            .filter(v -> Arrays.asList(UserRole.values())
                                    .stream()
                                    .map(e -> e.getValue())
                                    .collect(Collectors.toList())
                                    .contains(v.getId())
                            )
                            .map(v ->
                                    UserRole.fromValue(v.getId())
                            )
                            .collect(Collectors.toList()));
        }

        return user;

    }

    @Override
    public User findByUsername(String username) {
        UserEntity userEntity = userDao.findByUsernameIgnoreCase(username);
        if (userEntity == null) {
            return null;
        }

        User user = toUser(userEntity);

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = userDao.findOneByEmailIgnoreCase(email).map(this::toUser).orElse(null);
        return Optional.ofNullable(user);
    }

    private void sendRegistrationMail(RegistrationTokenEntity registrationTokenEntity, String email) throws MailNotSentException {
        try {
            MimeMessage message = sender.createMimeMessage();

            // Enable the multipart flag!
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String registratioTemplate = localeUtilsMessage.getErrorLocalizedMessage("activationMail", null);
            helper.setTo(email);
            String s = confirmUrl + registrationTokenEntity.getToken();
            helper.setText(registratioTemplate
                            .replace("@1", registrationTokenEntity.getUsername())
                            .replace("@2", registrationTokenEntity.getToken())
                    , true);
            helper.setSubject("Attivazione Account My Budget App");

            sender.send(message);
        } catch (Exception exception) {
            throw new MailNotSentException();
        }


    }

    @Override
    @Transactional
    public void registrateUser(UserRegistrationInfo registrationInfo) throws MailNotSentException {
        List<UserRoleEntity> roles =  Arrays.asList(UserRole.values()).stream().map((ur) -> {
            return UserRoleEntity.newBuilder().id(ur.getValue()).build();
        }).collect(Collectors.toList());

        UserEntity user = UserEntity.newBuilder()
                .username(registrationInfo.getUsername())
                .email(registrationInfo.getEmail())
                .password(passwordEncoder.encode(registrationInfo.getPassword()))
                .roles(roles)
                .status(UserEntity.UserStatusEnum.ToConfirm)
                .build();

        userDao.save(user);


        RegistrationTokenEntity registrationTokenEntity = createRegistrationToken(registrationInfo.getUsername());
        sendRegistrationMail(registrationTokenEntity, registrationInfo.getEmail());

    }

    private RegistrationTokenEntity createRegistrationToken(String username) {
        RegistrationTokenEntity registrationTokenEntity = RegistrationTokenEntity.newBuilder()
                .token("" + CommonUtils.generateRandomDigits(5))
                .expiredAt(OffsetDateTime.now().plus(registrationTokenExpiration, ChronoUnit.MINUTES))
                .username(username)
                .build();

        return registrationTokenEntityDao.save(registrationTokenEntity);

    }

    @Override
    public void validateUser(String username) throws UserNotActiveException {
        UserEntity userEntity = userDao.findByUsernameIgnoreCase(username);
        if (userEntity.getStatus() == null || userEntity.getStatus() != UserEntity.UserStatusEnum.Active) {
            throw new UserNotActiveException("user not active");
        }
    }

    @Transactional
    @Override
    public void confirmRegistration(String username, String registrationToken) throws NoSuchEntityException, ExpiredTokenException {
        OffsetDateTime now = OffsetDateTime.now();
        RegistrationTokenEntity registrationTokenEntity =
                registrationTokenEntityDao.findOneByTokenAndUsername(registrationToken, username).orElseThrow(NoSuchEntityException::new);

        if (now.isAfter(registrationTokenEntity.getExpiredAt())) {
            throw new ExpiredTokenException("Expired Token");
        }
        UserEntity userEntity = userDao.findByUsernameIgnoreCase(registrationTokenEntity.getUsername());
        if (userEntity == null) {
            throw new NoSuchEntityException();
        }
        userEntity.setStatus(UserEntity.UserStatusEnum.Active);
        userDao.save(userEntity);
        registrationTokenEntityDao.delete(registrationTokenEntity);
    }

    @Override
    @Transactional
    public void resendConfirmRegistrationMail(String username, String email) {
        RegistrationTokenEntity registrationTokenEntity =
                registrationTokenEntityDao.findOneByUsernameIgnoreCase(username).orElse(null);

        long l = -1;
        if (registrationTokenEntity != null) {
            l = registrationTokenEntityDao.deleteAllByUsernameIgnoreCase(registrationTokenEntity.getUsername());
        }
        logger.info("debug l [{}] " , l);


        registrationTokenEntity = createRegistrationToken(username);
        sendRegistrationMail(registrationTokenEntity, email);

    }
}
