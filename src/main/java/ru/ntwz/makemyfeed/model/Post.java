package ru.ntwz.makemyfeed.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.ntwz.makemyfeed.util.RandUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "posts")
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Length(max = 1024)
    private String content;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @Column
    private Instant createdAt = Instant.now();

    @Column
    private String uniqueLink = RandUtils.generateUniqueLink();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Post> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StorageEntry> attachments = new ArrayList<>();

    @Column
    private Boolean isDeleted = false;
}
