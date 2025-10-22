package com.exe.Huerta_directa.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "comments")
@Data
@Setter
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment", nullable = false, unique = true, updatable = false)
    private Long idComment;

    @Column(name = "comment_commenter", columnDefinition = "TEXT", nullable = false)
    private String commentCommenter;

    @Column(name = "creation_comment", nullable = false)
    private LocalDate creationComment = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_type", nullable = false, length = 20)
    private CommentType commentType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = true)
    private Product product;
}
