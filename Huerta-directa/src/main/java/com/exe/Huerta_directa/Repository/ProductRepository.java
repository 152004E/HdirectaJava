package com.exe.Huerta_directa.Repository;

import com.exe.Huerta_directa.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findBynameProduct(String nameProduct);

    Product findBycategory(String category);

}
