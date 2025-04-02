package com.mygradhub.mygradhubauth.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "tb_user")
@Entity(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private UserRole role;

    @Column
    private String profilePhoto;

    public User(String username, String email, String password, UserRole userRole, String profilePhoto) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = userRole;
        this.profilePhoto = profilePhoto;
    }

}
