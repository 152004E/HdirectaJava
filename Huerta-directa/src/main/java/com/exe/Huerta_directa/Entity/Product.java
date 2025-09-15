package com.exe.Huerta_directa.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table (name = "products")
@Data
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_product", nullable = false, unique = true, updatable = false)
    private Long idProduct;

    @Column (name = "name_product", nullable = false, length = 50)
    private String nameProduct;

    @Column (name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column (name = "category", nullable = false, length = 100)
    private String category;

    @Column (name = "image_product", nullable = false, length = 250)
    private String image_product;

    @Column (name = "unit", nullable = false, length = 250)
    private String unit;

    @Column (name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Lob
    @Column (name = "description_product", nullable = false)
    private String descriptionProduct;

}
