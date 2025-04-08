package com.mygradhub.mygradhubauth.configuration;

import com.mygradhub.mygradhubauth.infrastructure.configuration.swagger.SwaggerConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SwaggerConfigTest {

    @Test
    void shouldCreateOpenAPIWithExpectedInfoAndSecurity() {
        SwaggerConfig config = new SwaggerConfig();

        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI).isNotNull();

        Info info = openAPI.getInfo();
        assertThat(info).isNotNull();
        assertThat(info.getTitle()).isEqualTo("My GradHub Auth API");
        assertThat(info.getVersion()).isEqualTo("1.0");
        assertThat(info.getDescription()).isEqualTo("My GradHub Auth REST API");
        assertThat(info.getContact()).isNotNull();
        assertThat(info.getContact().getName()).isEqualTo("My Grad Hub Team, @mygradhubteam");

        assertThat(openAPI.getSecurity()).isNotEmpty();
        assertThat(openAPI.getSecurity().get(0).get("bearerAuth")).isNotNull();

        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertThat(securityScheme).isNotNull();
        assertThat(securityScheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
        assertThat(securityScheme.getScheme()).isEqualTo("bearer");
        assertThat(securityScheme.getBearerFormat()).isEqualTo("JWT");
    }
}
