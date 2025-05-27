package com.example.dairyinventoryservice.controller;

import com.example.dairyinventoryservice.dao.GetPurchaseDetailsDao;
import com.example.dairyinventoryservice.dto.request.InsertUserDto;
import com.example.dairyinventoryservice.dto.request.PasswordChangeDto;
import com.example.dairyinventoryservice.dto.request.UserAuthRequestDto;
import com.example.dairyinventoryservice.dto.response.AuthResponseDto;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;
import com.example.dairyinventoryservice.service.UserAuthService;
import com.example.dairyinventoryservice.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GetPurchaseDetailsDao getPurchaseDetailsDao;

    @PostMapping("/signUp")
    public GeneralResponse login(@RequestBody InsertUserDto insertUserDto) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = userAuthService.insertNewUser(insertUserDto);
        return generalResponse; //get otp and send through the email
    }

    //add the verified email by the user

    @PostMapping("/login")
    public GeneralResponse login(@RequestBody UserAuthRequestDto userAuthRequestDto) {

        UserAuthResponseDto user = userAuthService.authenticateUser(userAuthRequestDto.getEmail(), userAuthRequestDto.getPassword());
        GeneralResponse generalResponse = new GeneralResponse();

        if (user.getEmail() != null && user.getPassword() != null) {
            AuthResponseDto authResponseDto = new AuthResponseDto();
            log.info(user.getEmail());
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            authResponseDto.setToken(token);
            authResponseDto.setRoleId(user.getRole());
            authResponseDto.setFullName(getPurchaseDetailsDao.getUserImportantDetails(user.getEmail()).getFullName());
            authResponseDto.setEmail(user.getEmail());
            authResponseDto.setUserRoleName(getPurchaseDetailsDao.getUserImportantDetails(user.getEmail()).getUserRoleName());
            authResponseDto.setLocationName(getPurchaseDetailsDao.getUserImportantDetails(user.getEmail()).getLocationName());

            generalResponse.setData(authResponseDto);
            generalResponse.setRes(true);
            generalResponse.setMsg("Authenticated");
            generalResponse.setStatusCode(200);

        } else {
            generalResponse.setData(null);
            generalResponse.setMsg("Not-Authenticated");
            generalResponse.setStatusCode(403);
        }
        return generalResponse;
    }

    @PostMapping("/changePassword")
    public ResponseEntity<GeneralResponse> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = userAuthService.changePassword(passwordChangeDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }
}
