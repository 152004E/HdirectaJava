package com.exe.Huerta_directa.Repository;

import com.exe.Huerta_directa.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}


