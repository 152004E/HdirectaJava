package com.exe.Huerta_directa.DTO;

import java.util.ArrayList;
import java.util.List;

public class BulkEmailResponse {
    private int successCount;
    private int failureCount;
    private String message;
    private List<String> failedEmails;

    public BulkEmailResponse(int successCount, int failureCount, String message) {
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.message = message;
        this.failedEmails = new ArrayList<>();
    }

    // Getters y setters
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    public int getFailureCount() { return failureCount; }
    public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getFailedEmails() { return failedEmails; }
    public void setFailedEmails(List<String> failedEmails) { this.failedEmails = failedEmails; }
}
