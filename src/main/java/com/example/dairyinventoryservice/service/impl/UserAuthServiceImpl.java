package com.example.dairyinventoryservice.service.impl;

import com.example.dairyinventoryservice.dao.UserAuthDao;
import com.example.dairyinventoryservice.dto.request.InsertUserDto;
import com.example.dairyinventoryservice.dto.request.PasswordChangeDto;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;
import com.example.dairyinventoryservice.service.UserAuthService;
import com.example.dairyinventoryservice.util.GenerateOtpUtil;
import com.example.dairyinventoryservice.util.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserAuthDao userAuthDao;

    private int otp;



    @Override
    public GeneralResponse insertNewUser(InsertUserDto insertUserDto) {
        GeneralResponse generalResponse = new GeneralResponse();

        if (insertUserDto.getUserRoleId() == 3 || insertUserDto.getUserRoleId() == 4) {
            generalResponse = userAuthDao.insertNewUser(insertUserDto);
            otp=GenerateOtpUtil.generateOTP(); // Add only number 3 and 4 only (You are going to send this otp once it have created here you can validate with the otp)
        } else {
            generalResponse.setData(null);
            generalResponse.setMsg("Invalid User Type");
            generalResponse.setStatusCode(409);
        }

        return generalResponse;
    }

    // Add your service here for email validation and move the dao call to the new function

    @Override
    public UserAuthResponseDto authenticateUser(String email, String password) {
        UserAuthResponseDto userAuthResponseDto = new UserAuthResponseDto();

        userAuthResponseDto = userAuthDao.findByEmail(email);

        if (userAuthResponseDto.getEmail() == null) {
            log.error("User not found");

            userAuthResponseDto.setRole(0);
            userAuthResponseDto.setEmail(null);
            userAuthResponseDto.setPassword(null);
            return userAuthResponseDto;
        } else if (userAuthResponseDto.getPassword().equals(PasswordUtils.hashSHA256(password))) {

            log.info(userAuthResponseDto.getEmail());
            log.info(userAuthResponseDto.getPassword());
            log.info(userAuthResponseDto.getRole().toString());
            return userAuthResponseDto;
        } else {
            log.error("Invalid email or password");
            userAuthResponseDto.setRole(0);
            userAuthResponseDto.setEmail(null);
            userAuthResponseDto.setPassword(null);
            return userAuthResponseDto;
        }

    }

    @Override
    public GeneralResponse changePassword(PasswordChangeDto passwordChangeDto) {
       return userAuthDao.changePassword(passwordChangeDto);
    }
}
