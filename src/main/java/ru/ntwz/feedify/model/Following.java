package ru.ntwz.feedify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "followings")
@NoArgsConstructor
public class Following {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Following(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}