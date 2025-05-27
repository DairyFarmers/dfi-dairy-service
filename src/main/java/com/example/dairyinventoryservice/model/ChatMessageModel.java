package com.example.dairyinventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageModel {
    private String senderId;
    private String receiverId;
    private String content;
    private String sentAt;
}