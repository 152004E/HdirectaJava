package com.exe.Huerta_directa.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

    private Long idComment;
    private String nameCommenter;
    private String emailCommenter;
    private String commentCommenter;
    private LocalDate creationComment;
    private Long userId;      
    private Long productId;    
}
