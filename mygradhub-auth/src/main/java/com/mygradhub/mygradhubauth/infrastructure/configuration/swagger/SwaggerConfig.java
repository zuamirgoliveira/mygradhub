package com.mygradhub.mygradhubauth.infrastructure.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("My GradHub Auth API").version("1.0")
                .description("My GradHub Auth REST API")
                .contact(new Contact().name("My Grad Hub Team, @mygradhubteam"))
                .version("1.0"));
    }
}
