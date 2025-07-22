package com.example.demo;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:8081", exposedHeaders = "Authorization")
@Controller
public class ChatController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/private")
    public void sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        String senderName = principal.getName();
        chatMessage.setSender(senderName);
        messagingTemplate.convertAndSendToUser(chatMessage.getReceiver(), "/queue/messages", chatMessage);
    }
}
