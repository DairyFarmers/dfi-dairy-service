package com.example.dairyinventoryservice.dao;

import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;

public interface UserAuthDao {
    public UserAuthResponseDto findByEmail(String email);
}
