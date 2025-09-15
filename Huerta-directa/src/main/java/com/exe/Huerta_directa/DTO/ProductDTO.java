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
public class ProductDTO {

    private Long idProduct;
    private String nameProduct;
    private Double price;
    private String category;
    private String image_product;
    private String unidadMedida;
    private LocalDate publicationDate;
    private String descriptionProduct;
}


