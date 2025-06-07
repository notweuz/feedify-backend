package ru.ntwz.makemyfeed.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "attachments")
@Data
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column
    private String fileName;

    @Column
    private String filePath;
}
