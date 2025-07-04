package ru.ntwz.feedify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "storage")
@Entity
@Setter
@Getter
public class StorageEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String filePath;

    @Column
    private String uniqueName;

    @Column
    private String contentType;

    @Column
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
