package it.italiancoders.mybudgetrest.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import javax.validation.MessageInterpolator;
import java.util.Locale;


public class SpringMessageSourceMessageInterpolator implements MessageInterpolator,
        MessageSourceAware, InitializingBean {

    @Autowired
    @Qualifier("errorMessageSource")
    MessageSource errorMessageSource;

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return errorMessageSource.getMessage(messageTemplate, new Object[]{}, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return errorMessageSource.getMessage(messageTemplate, new Object[]{}, locale);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.errorMessageSource = errorMessageSource;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (errorMessageSource == null) {
            throw new IllegalStateException("MessageSource was not injected, could not initialize "
                    + this.getClass().getSimpleName());
        }
    }

}
