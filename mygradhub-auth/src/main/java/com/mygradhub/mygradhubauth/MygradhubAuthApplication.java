package com.mygradhub.mygradhubauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.mygradhub.mygradhubauth.domain.repository")
public class MygradhubAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MygradhubAuthApplication.class, args);
    }

}
