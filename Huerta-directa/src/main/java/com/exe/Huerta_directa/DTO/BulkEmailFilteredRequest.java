package com.exe.Huerta_directa.DTO;

import java.util.List;

public class BulkEmailFilteredRequest extends BulkEmailRequest {
    private List<Long> userIds;
    private List<String> emails;

    public List<Long> getUserIds() { return userIds; }
    public void setUserIds(List<Long> userIds) { this.userIds = userIds; }
    public List<String> getEmails() { return emails; }
    public void setEmails(List<String> emails) { this.emails = emails; }
}
