/*package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.MercadoPagoDTO;
import com.exe.Huerta_directa.DTO.MercadoPagoPaymentResponse;
import com.exe.Huerta_directa.Service.MercadoPagoService;
import com.exe.Huerta_directa.Service.OrderService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    private final MercadoPagoService mercadoPagoService;
    private final OrderService orderService;

    @Autowired
    public CheckoutController(MercadoPagoService mercadoPagoService, OrderService orderService) {
        this.mercadoPagoService = mercadoPagoService;
        this.orderService = orderService;
    }

    // 1. Crear preferencia (Checkout PRO)
    @PostMapping("/preference")
    public ResponseEntity<?> createPreference(@RequestBody MercadoPagoDTO checkoutData) {
        try {
            Map<String, String> preferenceData = mercadoPagoService.createPaymentPreference(checkoutData);
            Map<String,Object> resp = new HashMap<>();
            resp.put("status","success");
            resp.put("init_point", preferenceData.get("init_point"));
            resp.put("preference_id", preferenceData.get("preference_id"));
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException iae) {
            log.warn("Solicitud inválida: {}", iae.getMessage());
            return ResponseEntity.badRequest().body(Map.of("status","error","message", iae.getMessage()));
        } catch (MPApiException | MPException mp) {
            log.error("Error MercadoPago al crear preferencia", mp);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("status","error","message","Error creando preferencia en MercadoPago"));
        } catch (Exception e) {
            log.error("Error interno al crear preferencia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status","error","message","Error interno"));
        }
    }

    // 2. Process payment (Payment Brick) - acepta varias formas del body
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> payload) {
        try {
            if (payload == null || payload.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("status","error","message","Payload vacío"));
            }

            // Payment Brick puede enviar distintas estructuras. Intentamos extraer el payment id robustamente.
            Long paymentId = extractPaymentId(payload);
            if (paymentId == null) {
                log.warn("No fue posible extraer paymentId del payload: {}", payload);
                return ResponseEntity.badRequest().body(Map.of("status","error","message","No se encontró payment id en la petición"));
            }

            MercadoPagoPaymentResponse pago = mercadoPagoService.consultarPago(paymentId);
            if (pago == null) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("status","error","message","No se pudo consultar el pago en MercadoPago"));
            }

            Long orderId = pago.getExternalReference();
            String status = pago.getStatus();

            if (orderId == null) {
                // Si no hay external reference, no podemos confirmar orden: devolver info para investigación.
                log.warn("Pago sin external_reference numérico paymentId={}", paymentId);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status","error","message","Pago encontrado pero external_reference no es numérico o no existe"));
            }

            switch (status != null ? status.toLowerCase() : "") {
                case "approved":
                    orderService.confirmarPago(orderId, paymentId);
                    return ResponseEntity.ok(Map.of("status","success","payment_status","approved"));

                case "pending":
                    orderService.marcarPendiente(orderId);
                    return ResponseEntity.ok(Map.of("status","pending","payment_status","pending"));

                case "rejected":
                case "refused":
                    orderService.marcarRechazado(orderId);
                    return ResponseEntity.ok(Map.of("status","rejected","payment_status","rejected"));

                default:
                    log.warn("Estado de pago desconocido: {} (paymentId={})", status, paymentId);
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("status","error","message","Estado de pago desconocido: " + status));
            }

        } catch (Exception e) {
            log.error("Error procesando pago", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status","error","message","Error interno procesando pago"));
        }
    }

    // Helper para extraer payment id de payloads variados
    private Long extractPaymentId(Map<String, Object> payload) {
        try {
            // 1) Directo: { "id": 12345 }
            Object idObj = payload.get("id");
            if (idObj != null) {
                return parseLongSafe(idObj);
            }

            // 2) payload contiene "formData" (tu versión anterior) -> { formData: { payment_id: ... } }
            Object formDataObj = payload.get("formData");
            if (formDataObj instanceof Map) {
                Map<?,?> formData = (Map<?,?>) formDataObj;
                Object pid = formData.get("payment_id");
                if (pid == null) pid = formData.get("id");
                if (pid != null) return parseLongSafe(pid);
            }

            // 3) payload contiene "payment": { "id": 123 }
            Object paymentObj = payload.get("payment");
            if (paymentObj instanceof Map) {
                Map<?,?> paymentMap = (Map<?,?>) paymentObj;
                Object pid = paymentMap.get("id");
                if (pid != null) return parseLongSafe(pid);
            }

            // 4) payload.data.object.payment.id (webhooks estilo)
            Object dataObj = payload.get("data");
            if (dataObj instanceof Map) {
                Map<?,?> data = (Map<?,?>) dataObj;
                Object obj = data.get("object");
                if (obj instanceof Map) {
                    Map<?,?> objectMap = (Map<?,?>) obj;
                    Object paymentId = objectMap.get("id");
                    if (paymentId != null) return parseLongSafe(paymentId);
                }
            }

            // 5) field "payment_id" en raíz
            Object rootPaymentId = payload.get("payment_id");
            if (rootPaymentId != null) return parseLongSafe(rootPaymentId);

            return null;
        } catch (Exception e) {
            log.warn("Error extrayendo payment id del payload", e);
            return null;
        }
    }

    private Long parseLongSafe(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Number) return ((Number) value).longValue();
            String s = String.valueOf(value);
            // limpiar caracteres no numéricos comunes
            s = s.replaceAll("[^0-9]", "");
            if (s.isEmpty()) return null;
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            log.warn("No se pudo parsear a Long: {}", value);
            return null;
        }
    }

    // Rutas de redirección para Checkout PRO (si se usan)
    @GetMapping("/success")
    public ResponseEntity<?> success(@RequestParam(name="payment_id", required=false) Long payment_id,
                                     @RequestParam(name="external_reference", required=false) Long external_reference) {
        if (external_reference != null) orderService.confirmarPago(external_reference, payment_id);
        return ResponseEntity.ok("Pago aprobado. OrderID: " + external_reference);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> pending(@RequestParam(name="external_reference", required=false) Long external_reference) {
        if (external_reference != null) orderService.marcarPendiente(external_reference);
        return ResponseEntity.ok("Pago pendiente. OrderID: " + external_reference);
    }

    @GetMapping("/failure")
    public ResponseEntity<?> failure(@RequestParam(name="external_reference", required=false) Long external_reference) {
        if (external_reference != null) orderService.marcarRechazado(external_reference);
        return ResponseEntity.ok("Pago rechazado. OrderID: " + external_reference);
    }
}
*/