package com.example.dairyinventoryservice.dao.impl;

import com.example.dairyinventoryservice.dao.UserAuthDao;
import com.example.dairyinventoryservice.dto.response.UserAuthResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@Repository
public class UserAuthDaoImpl implements UserAuthDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserAuthResponseDto findByEmail(String email){
        UserAuthResponseDto userAuthResponseDto = null;

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_USER_AUTH_BY_EMAIL)) {

            callableStatement.setString(1, email);

            ResultSet resultSet = callableStatement.executeQuery();
            userAuthResponseDto = new UserAuthResponseDto();
            if (resultSet.next()) {
                userAuthResponseDto.setEmail(resultSet.getString("email"));
                userAuthResponseDto.setPassword(resultSet.getString("password"));
                userAuthResponseDto.setRole(resultSet.getInt("role"));
                log.info(userAuthResponseDto.getEmail());

            }

        }

        catch (SQLException e) {
            log.error("The is an issue occur while getting the user details by email id: {}",e.getMessage());
        }

        return userAuthResponseDto;
    }

}
