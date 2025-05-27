package com.example.dairyinventoryservice.repository.impl;

import com.example.dairyinventoryservice.model.ChatMessageModel;
import com.example.dairyinventoryservice.repository.ChatMessageRepository;
import com.example.dairyinventoryservice.dto.ChatMessage;
import com.example.dairyinventoryservice.data.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.datasource.DataSourceUtils;
import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveChatMessage(ChatMessage chatMessage) {
        try (Connection connection = DataSourceUtils.getConnection(
                Objects.requireNonNull(jdbcTemplate.getDataSource()
            ));
            CallableStatement callableStatement = connection.prepareCall(
                Constants.SAVE_CHAT_MESSAGE_PROCEDURE_NAME
            )) {
            callableStatement.setString(1, chatMessage.getSenderId());
            callableStatement.setString(2, chatMessage.getReceiverId());
            callableStatement.setString(3, chatMessage.getContent());
            callableStatement.execute();
            log.info("Chat message saved successfully: {}", chatMessage);
        } catch (SQLException e) {
            log.error("Failed to save chat message: {}", e.getMessage());
        }

        return;
    }

    public List<ChatMessageModel> getChatHistory(String user1, String user2) {
        List<ChatMessageModel> chatMessages = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(
                Objects.requireNonNull(jdbcTemplate.getDataSource())
            );
            CallableStatement callableStatement = connection.prepareCall(
                Constants.GET_CHAT_HISTORY_PROCEDURE_NAME
            )) {
            callableStatement.setString(1, user1);
            callableStatement.setString(2, user2);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                ChatMessageModel chatMessage = new ChatMessageModel();
                chatMessage.setSenderId(resultSet.getString("lSender"));
                chatMessage.setReceiverId(resultSet.getString("lReceiver"));
                chatMessage.setContent(resultSet.getString("lContent"));
                chatMessage.setSentAt(resultSet.getString("lSentAt"));
                chatMessages.add(chatMessage);
            }
        } catch (SQLException e) {
            log.error("Failed to get chat history: {}", e.getMessage());
            chatMessages.clear();
        }

        return chatMessages;
    }
}