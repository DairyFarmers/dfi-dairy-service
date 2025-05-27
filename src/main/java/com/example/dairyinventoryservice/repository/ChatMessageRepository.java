package com.example.dairyinventoryservice.repository;

import com.example.dairyinventoryservice.model.ChatMessageModel;
import com.example.dairyinventoryservice.dto.ChatMessage;
import java.util.List;

public interface ChatMessageRepository {
    public void saveChatMessage(ChatMessage chatMessage);
    public List<ChatMessageModel> getChatHistory(String user1, String user2);
}