package com.exe.Huerta_directa.Repository;

import com.exe.Huerta_directa.Entity.Product;
import com.exe.Huerta_directa.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByname(String name);

    User findByemail(String email);

    @Query("SELECT p FROM Product p JOIN FETCH p.user")
    List<Product> findAllProductsWithUser();

}
