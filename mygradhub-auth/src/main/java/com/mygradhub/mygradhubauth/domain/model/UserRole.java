package com.mygradhub.mygradhubauth.domain.model;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public boolean canDeleteUsers() {
        return this == ADMIN;
    }
}
