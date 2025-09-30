package com.exe.Huerta_directa.Repository;

import com.exe.Huerta_directa.Entity.Product;
import com.exe.Huerta_directa.Entity.User;
import org.modelmapper.internal.bytebuddy.dynamic.DynamicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByname(String name);

    Optional<User> findByEmail(String email);

    @Query("SELECT p FROM Product p JOIN FETCH p.user")
    List<Product> findAllProductsWithUser();

}
