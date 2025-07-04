package ru.ntwz.feedify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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
    @Length(max = 255)
    private String description;

    @Column
    @NotNull
    private String password;

    @Column
    private Instant registrationDate = Instant.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Following> following = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Following> followers = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private StorageEntry avatar;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "banner_id")
    private StorageEntry banner;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    public User(String displayName, String username, String password) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
    }
}
