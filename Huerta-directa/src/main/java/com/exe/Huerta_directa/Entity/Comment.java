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

    @Column(name = "name_commenter", length = 100, nullable = false)
    private String nameCommenter;

    @Column(name = "email_commenter", length = 100, nullable = false)
    private String emailCommenter;

    @Column(name = "comment_commenter", columnDefinition = "TEXT", nullable = false)
    private String commentCommenter;

    @Column(name = "creation_comment", nullable = false)
    private LocalDate creationComment = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
