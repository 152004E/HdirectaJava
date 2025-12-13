package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.Entity.ChatSocialMessage;

import java.util.List;

public interface ChatSocialService {

    ChatSocialMessage saveMessage(Long userId, String senderName, String content);

    List<ChatSocialMessage> getAllMessages();

    List<ChatSocialMessage> getMessagesByUser(Long userId);
}