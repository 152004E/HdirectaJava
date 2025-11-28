package com.exe.Huerta_directa.Impl;



import com.exe.Huerta_directa.Service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public void confirmarPago(Long orderId, Long paymentId) {
        // Lógica real
    }

    @Override
    public void marcarPendiente(Long orderId) {
        // Lógica real
    }

    @Override
    public void marcarRechazado(Long orderId) {
        // Lógica real
    }
}

