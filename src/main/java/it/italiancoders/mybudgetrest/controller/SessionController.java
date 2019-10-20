package it.italiancoders.mybudgetrest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.italiancoders.mybudgetrest.MybudgetRestApplication;
import it.italiancoders.mybudgetrest.dao.UserDao;
import it.italiancoders.mybudgetrest.exception.security.ExpiredTokenException;
import it.italiancoders.mybudgetrest.exception.security.MailNotSentException;
import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.exception.security.RestException;
import it.italiancoders.mybudgetrest.model.dto.LoginRequest;
import it.italiancoders.mybudgetrest.model.dto.Session;
import it.italiancoders.mybudgetrest.model.dto.User;
import it.italiancoders.mybudgetrest.model.dto.UserRegistrationInfo;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import it.italiancoders.mybudgetrest.service.local.LocaleUtilsMessage;
import it.italiancoders.mybudgetrest.service.security.JwtTokenManager;
import it.italiancoders.mybudgetrest.service.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Locale;

@RestController
public class SessionController {

    @Value("${jwt.accessToken.name}")
    private String accessTokenName;

    @Value("${jwt.refreshToken.name}")
    private String refreshTokenName;

    @Value("${spring.profiles.active}")
    private String enviroment;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserManager userManager;


    @Autowired
    private UserDao userDao;


    @Autowired
    LocaleUtilsMessage localeUtilsMessage;

    @RequestMapping(value = "public/v1/session/refresh/{refreshToken}", method  = RequestMethod.POST)
    public ResponseEntity<?> refreshToken( @PathVariable final String refreshToken,
                                                HttpServletResponse response) throws AuthenticationException {
        Locale locale = LocaleContextHolder.getLocale();

        String accessToken = null;
        try {
            accessToken = jwtTokenManager.generateAccessTokenByRefreshToken(refreshToken);
        } catch (Exception ex) {
            RestException exception  = RestException.newBuilder()
                    .title("Refresh Token Expired or Invalid")
                    .detail("Unable to refresh access token")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;
        }
        UserDetails userDetails = jwtTokenManager.getUserDetails(accessToken).orElseThrow(() ->
                RestException.newBuilder()
                        .title("Refresh Token Expired or Invalid")
                        .detail("Unable to refresh access token")
                        .status(HttpStatus.BAD_REQUEST)
                        .build());

        return ResponseEntity.ok(
                Session.newBuilder()
                        .userInfo((User) userDetails)
                        .locale(locale.getCountry())
                        .environment(enviroment)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());

    }
    protected static final Logger logger = LoggerFactory.getLogger(MybudgetRestApplication.class);

    @RequestMapping(value = "public/v1/session", method  = RequestMethod.POST)
    public ResponseEntity<Session> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest,
                                                             HttpServletResponse response) throws AuthenticationException, JsonProcessingException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Locale locale = LocaleContextHolder.getLocale();

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        userManager.validateUser(authenticationRequest.getUsername());
        final String accessToken = jwtTokenManager.generateAccessToken(userDetails);
        final String refreshToken = jwtTokenManager.generateRefreshToken(userDetails);


        return ResponseEntity.ok(
                Session.newBuilder()
                        .userInfo((User) userDetails)
                        .locale(locale.getCountry())
                        .environment(enviroment)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                .build());
    }

    @RequestMapping(value = "public/v1/registration-user", method  = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody @Valid  UserRegistrationInfo registrationInfo,
                                                             HttpServletResponse response) throws AuthenticationException, JsonProcessingException {
        User user = userManager.findByUsername(registrationInfo.getUsername());
        if (user != null) {
            RestException exception  = RestException.newBuilder()
                    .title(localeUtilsMessage.getErrorLocalizedMessage("SessionController.createUser.failed.title", null))
                    .detail(localeUtilsMessage.getErrorLocalizedMessage("SessionController.createUser.userAlreadyExist", null))
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;
        }

        user = userManager.findByEmail(registrationInfo.getEmail()).orElse(null);
        if (user != null) {
            RestException exception = RestException.newBuilder()
                    .title(localeUtilsMessage.getErrorLocalizedMessage("SessionController.createUser.failed.title", null))
                    .detail(localeUtilsMessage.getErrorLocalizedMessage("SessionController.createUser.emailAlreadyUsed", null))
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;
        }

        try {
            userManager.registrateUser(registrationInfo);
        } catch (MailNotSentException exc) {
            RestException exception  = RestException.newBuilder()
                    .title(localeUtilsMessage.getErrorLocalizedMessage("SessionController.createUser.failed.title", null))
                    .detail(localeUtilsMessage.getErrorLocalizedMessage("SessionController.createUser.mailNotSent", null))
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;
        }

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "public/v1/confirm-registration/{username}", method  = RequestMethod.POST)
    public ResponseEntity<?> confirmRegistration(@PathVariable final String username,
                                                 @RequestParam(value = "token", required = true) final String registrationToken) throws AuthenticationException, JsonProcessingException {
        if (StringUtils.isEmpty(registrationToken)) {
            RestException exception  = RestException.newBuilder()
                    .title("Conferma utente in errore")
                    .detail("E'  necessario il token di registrazione")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;
        }

        try {
            userManager.confirmRegistration(username, registrationToken);
        } catch (ExpiredTokenException ex) {

            RestException exception  = RestException.newBuilder()
                    .title("Conferma utente in errore")
                    .detail("Link di Attivazione Scaduto o Gia' Utilizzato: Richiedere un nuovo link di attivazione")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;

        } catch (NoSuchEntityException ex) {
            RestException exception  = RestException.newBuilder()
                    .title("Conferma utente in errore")
                    .detail("Token di Attivazione Invalido")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;
        }

        return ResponseEntity.ok("Utente Attivato, puoi iniziare ad utilizzare mybudget");

    }

    @RequestMapping(value = "public/v1/resend-confirm-registration-mail", method  = RequestMethod.POST)
    public ResponseEntity<?> resendConfirmRegistrationMail(@RequestParam final String username) throws AuthenticationException, JsonProcessingException {
        UserEntity user = userDao.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new NoSuchEntityException();
        }
        if (user.getStatus()== UserEntity.UserStatusEnum.Active) {
            RestException exception  = RestException.newBuilder()
                    .title("Reinvio Mail di conferma registrazione in errore")
                    .detail("E'  necessario il token di registrazione")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            throw exception;
        }

        userManager.resendConfirmRegistrationMail(username, user.getEmail());
        return ResponseEntity.noContent().build();

    }


}
