package com.exe.Huerta_directa.Repository;

import com.exe.Huerta_directa.Entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findBynameProduct(String nameProduct);

    // Metodo para buscar productos por el ID del usuario
    List<Product> findByUserId(Long userId);

    List<Product> findByNameProductContainingIgnoreCase(String nameProduct);

    List<Product> findByCategoryIgnoreCase(String category);

    // Método para verificar duplicados exactos
    boolean existsByNameProductIgnoreCaseAndCategoryIgnoreCase(String nameProduct, String category);

    // FETCH JOIN para traer usuarios eficientemente
    @org.springframework.data.jpa.repository.Query("SELECT p FROM Product p LEFT JOIN FETCH p.user")
    List<Product> findAllWithUsers();
}