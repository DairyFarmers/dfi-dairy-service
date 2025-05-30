package com.example.dairyinventoryservice.data.dao;

import com.example.dairyinventoryservice.model.dto.request.InsertUserDto;
import com.example.dairyinventoryservice.model.dto.request.PasswordChangeDto;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.model.dto.response.UserAuthResponseDto;

public interface UserAuthDao {
    public GeneralResponse insertNewUser(InsertUserDto insertUserDto);

    public UserAuthResponseDto findByEmail(String email);

    public GeneralResponse changePassword(PasswordChangeDto passwordChangeDto);
}
