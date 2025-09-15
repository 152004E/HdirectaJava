package com.exe.Huerta_directa.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table (name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_product", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column (name = "name_product", nullable = false, length = 50)
    private String nameProduct;


    private Double price;
    private String category;
    private String image_product;
    private String unidadMedida;
    private LocalDate publicationDate;
    private String descriptionProduct;

}
