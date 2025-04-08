package com.mygradhub.mygradhubauth.configuration;
import com.mygradhub.mygradhubauth.infrastructure.configuration.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    void shouldCreateBCryptPasswordEncoder() {
        SecurityConfig config = new SecurityConfig(null);
        PasswordEncoder encoder = config.passwordEncoder();

        assertThat(encoder).isNotNull();
        assertThat(encoder.encode("secret")).isNotBlank();
        assertThat(encoder.matches("secret", encoder.encode("secret"))).isTrue();
    }
}