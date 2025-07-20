package com.quiz.quizapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Benutzername darf nicht leer sein")
    @Size(min = 3, max = 50, message = "Benutzername muss zwischen 3 und 50 Zeichen lang sein")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Passwort darf nicht leer sein")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Rolle darf nicht leer sein")
    @Column(nullable = false)
    private String role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = "Email darf nicht leer sein")
    @Column(nullable = true)
    private String email;

    // Standardkonstruktor
    public User() {
    }

    // Konstruktor mit allen Feldern
    public User(Long id, String username, String password, String role, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    // Builder-Methode
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String password;
        private String role;
        private String email;

        UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(String role) {
            this.role = role;
            return this;
        }
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            return new User(id, username, password, role, email);
        }


    }
}