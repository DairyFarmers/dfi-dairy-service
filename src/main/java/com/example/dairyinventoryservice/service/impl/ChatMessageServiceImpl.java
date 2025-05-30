package com.example.dairyinventoryservice.service.impl;

import com.example.dairyinventoryservice.model.ChatMessageModel;
import com.example.dairyinventoryservice.model.dto.ChatMessage;
import com.example.dairyinventoryservice.service.ChatMessageService;
import com.example.dairyinventoryservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl(
        ChatMessageRepository chatMessageRepository
    ) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.saveChatMessage(chatMessage);
    }

    @Override
    public List<ChatMessageModel> getChatHistory(String user1, String user2) {
        return chatMessageRepository.getChatHistory(user1, user2);
    }
}