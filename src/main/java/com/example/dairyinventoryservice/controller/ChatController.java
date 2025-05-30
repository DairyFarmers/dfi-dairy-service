package com.example.dairyinventoryservice.config;

import com.example.dairyinventoryservice.model.ChatMessageModel;
import com.example.dairyinventoryservice.model.dto.ChatMessage;
import com.example.dairyinventoryservice.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.saveChatMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
            chatMessage.getReceiverId(),
            "/queue/messages",
            chatMessage
        );
    }

    @MessageMapping("/chat.addUser")
    public void addUser(
        @Payload ChatMessage chatMessage,
        SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
    }

    @GetMapping("/history")
    public List<ChatMessageModel> getChatHistory(
        @RequestParam String user1, 
        @RequestParam String user2) {
        return chatMessageService.getChatHistory(user1, user2);
    }

}