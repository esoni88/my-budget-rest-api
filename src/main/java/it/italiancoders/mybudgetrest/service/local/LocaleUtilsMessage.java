package it.italiancoders.mybudgetrest.service.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("localeUtilsMessage")
public class LocaleUtilsMessage {
    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource messageSource;

    public String getErrorLocalizedMessage(String keyMessage, Object[] params) {
        Locale locale = Locale.ITALIAN;
        try {
            return messageSource.getMessage(keyMessage, params, locale);
        } catch (NoSuchMessageException ex) {
            return "??" + keyMessage + "??";
        }
    }
}
