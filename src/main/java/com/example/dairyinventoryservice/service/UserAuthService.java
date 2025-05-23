package com.example.dairyinventoryservice.service;

import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;

public interface UserAuthService {
    public UserAuthResponseDto authenticateUser(String email, String password);
}
