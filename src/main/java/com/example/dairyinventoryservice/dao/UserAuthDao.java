package com.example.dairyinventoryservice.dao;

import com.example.dairyinventoryservice.dto.request.InsertUserDto;
import com.example.dairyinventoryservice.dto.request.PasswordChangeDto;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;

public interface UserAuthDao {
    public GeneralResponse insertNewUser(InsertUserDto insertUserDto);

    public UserAuthResponseDto findByEmail(String email);

    public GeneralResponse changePassword(PasswordChangeDto passwordChangeDto);
}
