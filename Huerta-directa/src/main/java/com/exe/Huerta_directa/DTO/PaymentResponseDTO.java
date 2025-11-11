package com.exe.Huerta_directa.DTO;

public class PaymentResponseDTO {
    private String initPoint;
    private String preferenceId;
    private String status;
    private String message;

    public PaymentResponseDTO() {}

    // getters y setters
    public String getInitPoint() { return initPoint; }
    public void setInitPoint(String initPoint) { this.initPoint = initPoint; }
    public String getPreferenceId() { return preferenceId; }
    public void setPreferenceId(String preferenceId) { this.preferenceId = preferenceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}