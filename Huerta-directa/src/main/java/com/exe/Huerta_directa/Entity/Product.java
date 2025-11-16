package com.exe.Huerta_directa.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


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
    @NotBlank
    @Size(max = 50)
    private String nameProduct = "sin nombre";

    @Column (name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column (name = "category", nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String category;

    @Column (name = "image_product", nullable = false, length = 250)
    @NotBlank
    @Size(max = 250)
    private String imageProduct = "sin nombre";

    @Column (name = "unit", nullable = false, length = 250)
    @NotBlank
    @Size(max = 250)
    private String unit = "Campo no rellenado";

    @Column (name = "publication_date", nullable = false)
    private LocalDate publicationDate =  LocalDate.now();

    @Column(name = "description_product", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String descriptionProduct = "sin descripcion";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

}
