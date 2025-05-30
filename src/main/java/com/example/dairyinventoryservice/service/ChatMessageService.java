package com.example.dairyinventoryservice.service;

import com.example.dairyinventoryservice.model.dto.ChatMessage;
import com.example.dairyinventoryservice.model.ChatMessageModel;
import java.util.List;

public interface ChatMessageService {
    public void saveChatMessage(ChatMessage chatMessage);
    public List<ChatMessageModel> getChatHistory(String user1, String user2);
}