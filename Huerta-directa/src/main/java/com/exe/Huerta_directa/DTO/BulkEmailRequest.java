package com.exe.Huerta_directa.DTO;

public class BulkEmailRequest {
    private String subject;
    private String body;

    public BulkEmailRequest() {}

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}