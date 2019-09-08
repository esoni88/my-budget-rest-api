package it.italiancoders.mybudgetrest.config;

import it.italiancoders.mybudgetrest.exception.security.ExpiredTokenException;
import it.italiancoders.mybudgetrest.exception.security.NoSuchEntityException;
import it.italiancoders.mybudgetrest.exception.security.RestException;
import it.italiancoders.mybudgetrest.exception.security.UserNotActiveException;
import it.italiancoders.mybudgetrest.model.dto.ConstraintError;
import it.italiancoders.mybudgetrest.model.dto.ErrorData;
import it.italiancoders.mybudgetrest.model.dto.ErrorInternal;
import it.italiancoders.mybudgetrest.model.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected final static Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource messageSource;


    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<?> handleInvalidGrantException(NoSuchEntityException rnfe, HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        ErrorResponse errorResponse = ErrorResponse.newBuilder().error(
                ErrorData.newBuilder()
                        .internal(
                                ErrorInternal.newBuilder()
                                        .exception(rnfe.getClass().getName())
                                        .stack(rnfe.getMessage())
                                        .build()
                        )
                        .userMessage(messageSource.getMessage("NoSuchEntityException.detail",null,locale))
                        .userTitle(messageSource.getMessage("NoSuchEntityException.title",null,locale))
                        .build()
        ).build();

        return new ResponseEntity<>(errorResponse, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<?> handleUserNotActiveException(UserNotActiveException rnfe, HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        ErrorResponse errorResponse = ErrorResponse.newBuilder().error(
                ErrorData.newBuilder()
                        .internal(
                                ErrorInternal.newBuilder()
                                        .exception(rnfe.getClass().getName())
                                        .stack(rnfe.getMessage())
                                        .build()
                        )
                        .userMessage(messageSource.getMessage("UserNotActiveException.detail",null,locale))
                        .userTitle(messageSource.getMessage("UserNotActiveException.title",null,locale))
                        .build()
        ).build();

        return new ResponseEntity<>(errorResponse, null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<?> handleExpiredTokenException(ExpiredTokenException rnfe, HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        ErrorResponse errorResponse = ErrorResponse.newBuilder().error(
                ErrorData.newBuilder()
                        .internal(
                                ErrorInternal.newBuilder()
                                        .exception(rnfe.getClass().getName())
                                        .stack(rnfe.getMessage())
                                        .build()
                        )
                        .userMessage(messageSource.getMessage("ExpiredTokenException.detail",null,locale))
                        .userTitle(messageSource.getMessage("ExpiredTokenException.title",null,locale))
                        .build()
        ).build();

        return new ResponseEntity<>(errorResponse, null, HttpStatus.ACCEPTED);
    }


    @ExceptionHandler({RestException.class})
    public ResponseEntity<Object> handleRestApiException(final RestException ex, final WebRequest request) {
        log.error(ex.getMessage(), ex);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stack = sw.toString().substring(0,300);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = ErrorResponse.newBuilder().error(
                ErrorData.newBuilder()
                        .internal(
                                ErrorInternal.newBuilder()
                                        .exception(ex.getClass().getName())
                                        .stack(stack)
                                        .build()
                        )
                        .userMessage(ex.getDetail())
                        .userTitle(ex.getTitle())
                        .build()
        ).build();


        return new ResponseEntity<>(errorResponse, headers, ex.getStatus());
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();

        ErrorResponse errorResponse = ErrorResponse.newBuilder().error(
                ErrorData.newBuilder()
                        .internal(
                                ErrorInternal.newBuilder()
                                        .exception(ex.getClass().getName())
                                        .stack(ex.getMessage())
                                        .build()
                        )
                        .userMessage(messageSource.getMessage("HttpMessageNotReadableException.detail",null,locale))
                        .userTitle(messageSource.getMessage("HttpMessageNotReadableException.title",null,locale))
                        .build()
        ).build();

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException manve,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();


        // Create ValidationError instances
        List<FieldError> fieldErrors =  manve.getBindingResult().getFieldErrors();
        List<ConstraintError> constraintsViolated = new ArrayList<>();
        if (fieldErrors != null) {
            Map<String, List<FieldError>> errorsMap = fieldErrors.stream().collect(groupingBy(FieldError::getField));
            constraintsViolated = errorsMap.keySet().stream()
                    .map((k) -> ConstraintError.newBuilder()
                            .fieldName(k)
                            .constraintsNotRespected(errorsMap.get(k).stream().map((v) -> v.getDefaultMessage()).collect(Collectors.toList()))
                            .build()
                    ).collect(Collectors.toList());
        }

        ErrorData error = ErrorData.newBuilder()
                .userTitle(messageSource.getMessage("ValidationFailed.title",null,locale))
                .userMessage(messageSource.getMessage("ValidationFailed.detail",null,locale))
                .constraintErrors(constraintsViolated)
                .build();

        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }

}
