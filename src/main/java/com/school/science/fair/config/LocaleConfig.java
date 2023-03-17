package com.school.science.fair.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {

  @Bean(name = "localeResolver")
  public LocaleResolver localeResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(new Locale("pt", "BR"));
    return sessionLocaleResolver;
  }
  
  @Bean
  public MessageSource messageSource() {
      ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
      messageSource.setDefaultEncoding("UTF-8");
      messageSource.setBasename("classpath:messages/messages");
      return messageSource;
  }
  
}