package com.example.dairyinventoryservice.controller;

import com.example.dairyinventoryservice.dto.request.UserAuthRequestDto;
import com.example.dairyinventoryservice.dto.response.AuthResponseDto;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;
import com.example.dairyinventoryservice.service.UserAuthService;
import com.example.dairyinventoryservice.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public GeneralResponse login(@RequestBody UserAuthRequestDto userAuthRequestDto){

        UserAuthResponseDto user = userAuthService.authenticateUser(userAuthRequestDto.getEmail(), userAuthRequestDto.getPassword());
        GeneralResponse generalResponse = new GeneralResponse();

        if(user.getEmail()!= null) {
            AuthResponseDto authResponseDto = new AuthResponseDto();
            log.info(user.getEmail());
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            authResponseDto.setToken(token);
            authResponseDto.setEmail(user.getEmail());
            authResponseDto.setRole(user.getRole());

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
}
