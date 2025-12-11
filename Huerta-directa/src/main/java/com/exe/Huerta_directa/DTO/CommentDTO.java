package com.exe.Huerta_directa.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.exe.Huerta_directa.Entity.CommentType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

    private Long idComment;
    private String commentCommenter;
    private LocalDate creationComment;
    private CommentType commentType;
    private Long userId;
    private String nameCommenter;
    private Long productId;

}
