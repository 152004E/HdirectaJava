package com.exe.Huerta_directa.Service;


import org.springframework.stereotype.Service;


public interface OrderService {
    void confirmarPago(Long orderId, Long paymentId);
    void marcarPendiente(Long orderId);
    void marcarRechazado(Long orderId);
}
