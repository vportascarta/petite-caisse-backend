package com.nivtech.petitecaisse;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.nivtech.petitecaisse.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableJpaRepositories("com.nivtech.petitecaisse.repository")
@ComponentScan(basePackages = {"com.nivtech.petitecaisse.*"})
@EntityScan("com.nivtech.petitecaisse.*")
public class PetitecaisseApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(PetitecaisseApplication.class, args);
    }

    @Bean
    public com.fasterxml.jackson.databind.Module datatypeHibernateModule()
    {
        return new Hibernate5Module();
    }

    @Bean
    public MessageSource messageSource()
    {
        Locale.setDefault(Locale.CANADA_FRENCH);
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages");
        return messageSource;
    }
}
