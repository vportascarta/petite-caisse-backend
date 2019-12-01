package com.nivtech.petitecaisse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/dashboard")
                .setViewName("forward:/");
        registry.addViewController("/signup")
                .setViewName("forward:/");
        registry.addViewController("/signin")
                .setViewName("forward:/");
        registry.addViewController("/signout")
                .setViewName("forward:/");
        registry.addViewController("/purchase")
                .setViewName("forward:/");
        registry.addViewController("/history")
                .setViewName("forward:/");
        registry.addViewController("/account")
                .setViewName("forward:/");
        registry.addViewController("/admin/register")
                .setViewName("forward:/");
        registry.addViewController("/admin/user")
                .setViewName("forward:/");
        registry.addViewController("/admin/product")
                .setViewName("forward:/");
        registry.addViewController("/admin/transaction")
                .setViewName("forward:/");
        registry.addViewController("/pw-forget")
                .setViewName("forward:/");
        registry.addViewController("/pw-change")
                .setViewName("forward:/");
        registry.addViewController("/oauth2/redirect")
                .setViewName("forward:/");
        registry.addViewController("/pos")
                .setViewName("forward:/");
        registry.addViewController("/tos")
                .setViewName("forward:/");

    }
}
