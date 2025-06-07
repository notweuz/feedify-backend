package ru.ntwz.makemyfeed.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String displayName;

    @Column
    @NotNull
    private String username;

    @Column
    @NotNull
    private String password;

    @Column
    private Instant registrationDate = Instant.now();

    public User(String displayName, String username, String password) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
    }
}
