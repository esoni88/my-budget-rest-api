package it.italiancoders.mybudgetrest.config;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;


@Configuration
public class I18nConfig    implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        SmartLocaleResolver smartLocaleResolver= new SmartLocaleResolver();
        smartLocaleResolver.setDefaultLocale(Locale.ITALIAN);
        return  smartLocaleResolver;
    }

    @Bean(name="errorMessageSource")
    @Primary
    public MessageSource errorMessageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/i18n/errors/message","classpath:/i18n/errors/validations", "classpath:/i18n/errors/tags");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}
