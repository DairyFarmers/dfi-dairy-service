package com.example.dairyinventoryservice.data.dao.impl;

import com.example.dairyinventoryservice.data.dao.DaoConstant;
import com.example.dairyinventoryservice.data.dao.PostPurchaseDetailsDao;
import com.example.dairyinventoryservice.model.dto.request.*;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.model.dto.response.InsertDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Objects;

@Slf4j
@Repository
public class PostPurchaseDetailsDaoImpl implements PostPurchaseDetailsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public GeneralResponse addItem(ItemsDetailsDto itemDto) {

        InsertDetailsResponse insertPurchaseDetailsResponse = new InsertDetailsResponse();
        GeneralResponse generalResponse = new GeneralResponse();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource())); CallableStatement callableStatement = connection.prepareCall(DaoConstant.INSERT_ITEM_DETAIL)) {

            callableStatement.setObject(1, itemDto.getItemName(), Types.VARCHAR);
            callableStatement.setObject(2, itemDto.getItemExpiryDuration(), Types.VARCHAR);
            callableStatement.setBigDecimal(3,itemDto.getMaxPurchaseItemPrice());
            callableStatement.setBigDecimal(4, itemDto.getMaxSellingItemPrice());
            callableStatement.setBigDecimal(5,itemDto.getMaxUnitPriceForB2b());

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                insertPurchaseDetailsResponse.setRDataUpdated(resultSet.getString("rDataUpdated"));
            }
            generalResponse.setData(insertPurchaseDetailsResponse.getRDataUpdated());
            if (Objects.equals(insertPurchaseDetailsResponse.getRDataUpdated(), "Item Details Successfully Inserted")) {
                generalResponse.setMsg("Successfully data inserted");
                generalResponse.setStatusCode(201);
                generalResponse.setRes(true);
            } else {
                generalResponse.setMsg("Error in inserting the new item");
            }
        } catch (SQLException e) {
            log.error("----------------------------------------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());
        }

        return generalResponse;

    }

    @Override
    public GeneralResponse addUserRole(UserRoleDto userRoleDto) {
        InsertDetailsResponse insertPurchaseDetailsResponse = new InsertDetailsResponse();
        GeneralResponse generalResponse = new GeneralResponse();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource())); CallableStatement callableStatement = connection.prepareCall(DaoConstant.INSERT_USER_ROLE)) {

            callableStatement.setObject(1, userRoleDto.getRole(), Types.VARCHAR);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                insertPurchaseDetailsResponse.setRDataUpdated(resultSet.getString("rDataUpdated"));
            }

            generalResponse.setData(insertPurchaseDetailsResponse.getRDataUpdated());

            if (Objects.equals(insertPurchaseDetailsResponse.getRDataUpdated(), "User Role Successfully Inserted")) {
                generalResponse.setMsg("Successfully data inserted");
                generalResponse.setStatusCode(201);
                generalResponse.setRes(true);
            } else {
                generalResponse.setMsg("Error in inserting the user role");
            }
        } catch (SQLException e) {
            log.error("----------------------------------------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());
        }
        return generalResponse;
    }

    @Override
    public GeneralResponse addLocation(LocationDto locationDto) {
        InsertDetailsResponse insertPurchaseDetailsResponse = new InsertDetailsResponse();
        GeneralResponse generalResponse = new GeneralResponse();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource())); CallableStatement callableStatement = connection.prepareCall(DaoConstant.INSERT_LOCATION)) {

            callableStatement.setObject(1, locationDto.getLocationName(), Types.VARCHAR);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                insertPurchaseDetailsResponse.setRDataUpdated(resultSet.getString("rDataUpdated"));
            }

            generalResponse.setData(insertPurchaseDetailsResponse.getRDataUpdated());


            if (Objects.equals(insertPurchaseDetailsResponse.getRDataUpdated(), "Location Details Successfully Inserted")) {

                generalResponse.setMsg("Successfully data inserted");
                generalResponse.setStatusCode(201);
                generalResponse.setRes(true);
            } else {
                generalResponse.setMsg("Error in inserting the location");

            }

        } catch (SQLException e) {
            log.error("----------------------------------------------------------------------------------------------------{}, --------{}", insertPurchaseDetailsResponse.getRDataUpdated(), e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());
        }
        return generalResponse;
    }

    @Override
    public GeneralResponse updateUser(UpdateUserDto updateUserDto) {
        InsertDetailsResponse insertPurchaseDetailsResponse = new InsertDetailsResponse();
        GeneralResponse generalResponse = new GeneralResponse();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource())); CallableStatement callableStatement = connection.prepareCall(DaoConstant.UPDATE_USER)) {

            callableStatement.setObject(1, updateUserDto.getEmail(), Types.VARCHAR);
            callableStatement.setObject(2, updateUserDto.getMobileNumber(), Types.VARCHAR);
            callableStatement.setObject(3, updateUserDto.getFirstName(), Types.VARCHAR);
            callableStatement.setObject(4, updateUserDto.getLastName(), Types.VARCHAR);
            callableStatement.setInt(5, updateUserDto.getUserRoleId());
            callableStatement.setInt(6, updateUserDto.getLocationID());

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                insertPurchaseDetailsResponse.setRDataUpdated(resultSet.getString("rDataUpdated"));
            }

            generalResponse.setData(insertPurchaseDetailsResponse.getRDataUpdated());
            if (Objects.equals(insertPurchaseDetailsResponse.getRDataUpdated(), "User Details Successfully Updated")) {
                generalResponse.setMsg("Successfully data inserted");
                generalResponse.setStatusCode(201);
                generalResponse.setRes(true);
            } else {
                generalResponse.setMsg("Error in updating the user details");
            }

        } catch (SQLException e) {
            log.error("----------------------------------------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());

        }
        return generalResponse;
    }

    @Override
    public GeneralResponse addNewUser(InsertUserDto insertUserDto) {
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
    public GeneralResponse addNewPurchaseDetails(PurchaseDetailDto insertPurchase) {
        InsertDetailsResponse insertPurchaseDetailsResponse = new InsertDetailsResponse();
        GeneralResponse generalResponse = new GeneralResponse();
        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.INSERT_PURCHASE)) {
            callableStatement.setObject(1, insertPurchase.getInsertedBy(), Types.INTEGER);
            callableStatement.setBigDecimal(2, insertPurchase.getPurchasePrice());
            callableStatement.setObject(3, insertPurchase.getItemId(), Types.INTEGER);
            callableStatement.setObject(4, insertPurchase.getSellerId(), Types.INTEGER);
            callableStatement.setObject(5, insertPurchase.getItemCount(), Types.INTEGER);
            callableStatement.setObject(6, insertPurchase.getInventoryLocationId(), Types.INTEGER);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                insertPurchaseDetailsResponse.setRDataUpdated(resultSet.getString("rDataUpdated"));
            }

            generalResponse.setData(insertPurchaseDetailsResponse.getRDataUpdated());
            if (Objects.equals(insertPurchaseDetailsResponse.getRDataUpdated(), "Purchase Details Successfully Inserted")) {
                generalResponse.setMsg("Successfully data inserted");
                generalResponse.setStatusCode(201);
                generalResponse.setRes(true);
            } else {
                generalResponse.setMsg("Error in inserting the purchase Details");
            }
        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());

        }
        return generalResponse;
    }

    @Override
    public GeneralResponse addNewSalesDetails(SalesDetailsDto insertSalesDetails) {
        InsertDetailsResponse insertPurchaseDetailsResponse = new InsertDetailsResponse();
        GeneralResponse generalResponse = new GeneralResponse();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.INSERT_SALES_DETAILS)) {

            callableStatement.setInt(1, insertSalesDetails.getItemID());
            callableStatement.setInt(2, insertSalesDetails.getItemCount());
            callableStatement.setInt(3, insertSalesDetails.getInventoryLocationId());
            callableStatement.setBigDecimal(4, insertSalesDetails.getSalesPrice());
            callableStatement.setInt(5, insertSalesDetails.getSellerId());
            callableStatement.setBoolean(6, insertSalesDetails.isB2b());

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                insertPurchaseDetailsResponse.setRDataUpdated(resultSet.getString("rDataUpdated"));
            }

            generalResponse.setData(insertPurchaseDetailsResponse.getRDataUpdated());
            if (Objects.equals(insertPurchaseDetailsResponse.getRDataUpdated(), "Sales Data Updated")) {
                generalResponse.setMsg("Successfully data inserted");
                generalResponse.setStatusCode(201);
                generalResponse.setRes(true);
            } else {
                generalResponse.setMsg("Error in inserting the sales Details");
            }
        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());

        }

        return generalResponse;
    }


}