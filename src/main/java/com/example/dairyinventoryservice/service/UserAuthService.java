package com.example.dairyinventoryservice.service;

import com.example.dairyinventoryservice.model.dto.request.InsertUserDto;
import com.example.dairyinventoryservice.model.dto.request.PasswordChangeDto;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.model.dto.response.UserAuthResponseDto;

public interface UserAuthService {

    public GeneralResponse insertNewUser(InsertUserDto insertUserDto);

    // Add your new service here..........................................................

    public UserAuthResponseDto authenticateUser(String email, String password);

    public GeneralResponse changePassword(PasswordChangeDto passwordChangeDto);
}
