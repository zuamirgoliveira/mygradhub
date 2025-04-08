package com.mygradhub.mygradhubauth.configuration;

import com.mygradhub.mygradhubauth.infrastructure.configuration.security.SecurityConfig;
import com.mygradhub.mygradhubauth.infrastructure.security.SecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class SecurityConfigUnitTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        SecurityFilter mockFilter = mock(SecurityFilter.class);
        securityConfig = new SecurityConfig(mockFilter);
    }

    @Test
    void shouldCreateSecurityFilterChain() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, Mockito.RETURNS_DEEP_STUBS);

        // Aqui estamos apenas testando se o bean é criado sem exception
        SecurityFilterChain result = securityConfig.configure(httpSecurity);

        assertNotNull(result);
    }

    @Test
    void shouldCreatePasswordEncoder() {
        assertNotNull(securityConfig.passwordEncoder());
    }
}
