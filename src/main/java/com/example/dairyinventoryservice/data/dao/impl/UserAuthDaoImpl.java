package com.example.dairyinventoryservice.data.dao.impl;

import com.example.dairyinventoryservice.data.dao.DaoConstant;
import com.example.dairyinventoryservice.data.dao.UserAuthDao;
import com.example.dairyinventoryservice.model.dto.request.InsertUserDto;
import com.example.dairyinventoryservice.model.dto.request.PasswordChangeDto;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.model.dto.response.InsertDetailsResponse;
import com.example.dairyinventoryservice.model.dto.response.UserAuthResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Objects;

@Slf4j
@Repository
public class UserAuthDaoImpl implements UserAuthDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public GeneralResponse insertNewUser(InsertUserDto insertUserDto) {
        InsertDetailsResponse insertPurchaseDetailsResponse = new InsertDetailsResponse();
        GeneralResponse generalResponse = new GeneralResponse();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource())); CallableStatement callableStatement = connection.prepareCall(DaoConstant.INSERT_USER)) {

            callableStatement.setObject(1, insertUserDto.getFirstName(), Types.VARCHAR);
            callableStatement.setObject(2, insertUserDto.getLastName(), Types.VARCHAR);
            callableStatement.setInt(3, insertUserDto.getUserRoleId());
            callableStatement.setObject(4, insertUserDto.getEmail(), Types.VARCHAR);
            callableStatement.setObject(5, insertUserDto.getPhoneNumber(), Types.VARCHAR);
            callableStatement.setInt(6, insertUserDto.getLocationId());
            callableStatement.setObject(7,insertUserDto.getPassword(), Types.VARCHAR);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                insertPurchaseDetailsResponse.setRDataUpdated(resultSet.getString("rDataUpdated"));
            }

            generalResponse.setData(insertPurchaseDetailsResponse.getRDataUpdated());
            if (Objects.equals(insertPurchaseDetailsResponse.getRDataUpdated(), "User Details Successfully Inserted")) {
                generalResponse.setMsg("Successfully data inserted");
                generalResponse.setStatusCode(201);
                generalResponse.setRes(true);
            } else {
                generalResponse.setMsg("Error in inserting new user");
            }

        } catch (SQLException e) {
            log.error("----------------------------------------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());
        }
        return generalResponse;

    }

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

    @Override
    public GeneralResponse changePassword(PasswordChangeDto passwordChangeDto){
        GeneralResponse generalResponse = new GeneralResponse();
        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.CHANGE_PASSWORD)) {
            callableStatement.setObject(1, passwordChangeDto.getEmailId(), Types.VARCHAR);
            callableStatement.setObject(2, passwordChangeDto.getOldPassword(), Types.VARCHAR);
            callableStatement.setObject(3, passwordChangeDto.getNewPassword(), Types.VARCHAR);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                generalResponse.setData(resultSet.getString(1));
                generalResponse.setMsg("Successfully changed password");
                generalResponse.setStatusCode(200);
                generalResponse.setRes(true);
            }

            else {
                generalResponse.setData(null);
                generalResponse.setMsg("Error in inserting the sales Details");

            }
        }

        catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setRes(false);
            generalResponse.setData("Input valid data");
            generalResponse.setMsg(e.getMessage());
            generalResponse.setStatusCode(409);
        }

        return generalResponse;

    }

}