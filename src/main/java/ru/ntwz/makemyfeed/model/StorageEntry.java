package ru.ntwz.makemyfeed.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "storage")
@Entity
@Data
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
}
