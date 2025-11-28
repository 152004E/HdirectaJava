package com.exe.Huerta_directa.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class PaymentResponse {

    private String status;
    private String preferenceId;
    private Map<String, Object> raw;

    public PaymentResponse() {}

    public PaymentResponse(String status, String preferenceId, Map<String, Object> raw) {
        this.status = status;
        this.preferenceId = preferenceId;
        this.raw = raw;
    }

    public String getStatus() { return status; }
    public String getPreferenceId() { return preferenceId; }
    public Map<String, Object> getRaw() { return raw; }

    public void setStatus(String status) { this.status = status; }
    public void setPreferenceId(String preferenceId) { this.preferenceId = preferenceId; }
    public void setRaw(Map<String, Object> raw) { this.raw = raw; }
}
