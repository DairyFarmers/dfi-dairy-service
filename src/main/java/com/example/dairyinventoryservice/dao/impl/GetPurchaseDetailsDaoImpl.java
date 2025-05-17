package com.example.dairyinventoryservice.dao.impl;

import com.example.dairyinventoryservice.dao.GetPurchaseDetailsDao;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.dto.response.GetUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class GetPurchaseDetailsDaoImpl implements GetPurchaseDetailsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public GeneralResponse getUserDetails(String emailId) {
        GeneralResponse generalResponse = new GeneralResponse();
        GetUserDetails getUserDetails  = null;
        List<GetUserDetails> getUserDetailsList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_USER_DETAIL_BY_EMAIL_ID)) {

            callableStatement.setString(1, emailId);

            ResultSet resultSet = callableStatement.executeQuery();

            if(resultSet != null) {
                while (resultSet.next()) {
                    getUserDetails  = new GetUserDetails();
                    getUserDetails.setFullName(resultSet.getString("lfullname"));
                    getUserDetails.setMobileNumber(resultSet.getString("lmobilenumber"));
                    getUserDetails.setUserRole(resultSet.getString("luserrole"));
                    getUserDetails.setUserRoleId(resultSet.getInt("luserroleid"));
                    getUserDetails.setLocationName(resultSet.getString("luserlocation"));
                    getUserDetailsList.add(getUserDetails);
                }
                generalResponse.setData(getUserDetailsList);
                generalResponse.setMsg("Success");
                generalResponse.setStatusCode(400);
                generalResponse.setRes(true);
            }

        } catch (SQLException e) {

            log.error("-------------------------------{}", e.getMessage());
            generalResponse.setMsg(e.getMessage());

        }
        return generalResponse;
    }
}
