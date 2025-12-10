package com.exe.Huerta_directa.Controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.exe.Huerta_directa.DTO.ChatMessage;

@Controller
public class ChatWebSocketController {
    @MessageMapping("/sendMessage")  // Cliente envía a /app/sendMessage
    @SendTo("/topic/messages")   // el servidor reenvia a /tipic/messages

    public ChatMessage send(ChatMessage message){
        System.out.println("");
        return message;
    }
}
