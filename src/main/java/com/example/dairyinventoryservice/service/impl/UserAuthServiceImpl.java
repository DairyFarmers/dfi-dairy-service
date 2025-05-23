package com.example.dairyinventoryservice.service.impl;

import com.example.dairyinventoryservice.dao.UserAuthDao;
import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;
import com.example.dairyinventoryservice.service.UserAuthService;
import com.example.dairyinventoryservice.util.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserAuthDao userAuthDao;

    public UserAuthResponseDto authenticateUser(String email, String password){
      UserAuthResponseDto userAuthResponseDto = new UserAuthResponseDto();

      userAuthResponseDto = userAuthDao.findByEmail(email);

        if(userAuthResponseDto.getEmail() == null){
            log.error("User not found");
            userAuthResponseDto.setRole(0);
            userAuthResponseDto.setEmail(null);
            userAuthResponseDto.setPassword(null);
            return userAuthResponseDto;
        }

        else if(userAuthResponseDto.getPassword().equals(PasswordUtils.hashSHA256(password))){
            log.info(userAuthResponseDto.getEmail());
            log.info(userAuthResponseDto.getPassword());
            log.info(userAuthResponseDto.getRole().toString());
            return userAuthResponseDto;
        }

        else {
            log.error("Invalid email or password");
            userAuthResponseDto.setRole(0);
            userAuthResponseDto.setEmail(null);
            userAuthResponseDto.setPassword(null);
            return userAuthResponseDto;
        }

    }
}
