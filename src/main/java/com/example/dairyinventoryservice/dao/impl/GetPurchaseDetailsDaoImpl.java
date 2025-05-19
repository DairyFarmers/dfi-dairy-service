package com.example.dairyinventoryservice.dao.impl;

import com.example.dairyinventoryservice.dao.GetPurchaseDetailsDao;
import com.example.dairyinventoryservice.dto.response.*;
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
        GetUserDetails getUserDetails = null;
        List<GetUserDetails> getUserDetailsList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_USER_DETAIL_BY_EMAIL_ID)) {

            callableStatement.setString(1, emailId);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    getUserDetails = new GetUserDetails();
                    getUserDetails.setFullName(resultSet.getString("lfullname"));
                    getUserDetails.setMobileNumber(resultSet.getString("lmobilenumber"));
                    getUserDetails.setUserRole(resultSet.getString("luserrole"));
                    getUserDetails.setUserRoleId(resultSet.getInt("luserroleid"));
                    getUserDetails.setLocationName(resultSet.getString("luserlocation"));
                    getUserDetailsList.add(getUserDetails);
                }
                generalResponse.setData(getUserDetailsList);
                generalResponse.setMsg("Success");
                generalResponse.setStatusCode(200);
                generalResponse.setRes(true);
            }

        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());
        }
        return generalResponse;
    }

    @Override
    public GeneralResponse getPurchaseDetails(String startDate, String endDate) {
        GeneralResponse generalResponse = new GeneralResponse();
        GetPurchaseDetailsResponse getPurchaseDetailsResponse = null;
        List<GetPurchaseDetailsResponse> getPurchaseDetailsList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_PURCHASE_DETAILS_BY_DATE_RANGE)) {

            callableStatement.setString(1, startDate);
            callableStatement.setString(2, endDate);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    getPurchaseDetailsResponse = new GetPurchaseDetailsResponse();
                    getPurchaseDetailsResponse.setRCreatedOn(resultSet.getString("rcreatedon"));
                    getPurchaseDetailsResponse.setRInventoryLocationName(resultSet.getString("rinventorylocationname"));
                    getPurchaseDetailsResponse.setRCreatedBy(resultSet.getString("rcreatedby"));
                    getPurchaseDetailsResponse.setRExpiryDate(resultSet.getString("rexpirydate"));
                    getPurchaseDetailsResponse.setRItemName(resultSet.getString("ritemname"));
                    getPurchaseDetailsResponse.setRItemCount(resultSet.getInt("ritemcount"));
                    getPurchaseDetailsResponse.setRSoldBy(resultSet.getString("rsoldby"));
                    getPurchaseDetailsList.add(getPurchaseDetailsResponse);
                }
                generalResponse.setData(getPurchaseDetailsList);
                generalResponse.setMsg("Success");
                generalResponse.setStatusCode(200);
                generalResponse.setRes(true);
            }

        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Input valid fields");
            generalResponse.setMsg(e.getMessage());
        }

        return generalResponse;
    }

    @Override
    public GeneralResponse getItemDetails() {
        GeneralResponse generalResponse = new GeneralResponse();

        GetItemDetailsDto getItemDetailsDto = null;
        List<GetItemDetailsDto> getItemDetailsDtoList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_ITEM_DETAILS)) {

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    getItemDetailsDto = new GetItemDetailsDto();

                    getItemDetailsDto.setItemName(resultSet.getString("ritemname"));
                    getItemDetailsDto.setItemId(resultSet.getInt("ritemid"));
                    getItemDetailsDto.setItemExpiryDuration(resultSet.getString("ritemexpiryduration"));
                    getItemDetailsDtoList.add(getItemDetailsDto);
                }
                generalResponse.setData(getItemDetailsDtoList);
                generalResponse.setMsg("Success");
                generalResponse.setStatusCode(200);
                generalResponse.setRes(true);
            }

        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Error occur while getting the item details");
            generalResponse.setMsg(e.getMessage());
        }

        return generalResponse;
    }

    @Override
    public GeneralResponse getLocationDetails() {
        GeneralResponse generalResponse = new GeneralResponse();
        GetLocationDto getLocationDto = null;
        List<GetLocationDto> getLocationDtoList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_LOCATION_DETAILS)) {

            ResultSet resultSet = callableStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    getLocationDto = new GetLocationDto();
                    getLocationDto.setLocationName(resultSet.getString("rlocationname"));
                    getLocationDto.setLocationId(resultSet.getInt("rlocationid"));
                    getLocationDtoList.add(getLocationDto);
                }
            }
            generalResponse.setData(getLocationDtoList);
            generalResponse.setMsg("Success");
            generalResponse.setStatusCode(200);
            generalResponse.setRes(true);

        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Error occur while getting location details");
            generalResponse.setMsg(e.getMessage());
        }
        return generalResponse;
    }

    @Override
    public GeneralResponse getExpiredAndWillExpireProducts() {
        GeneralResponse generalResponse = new GeneralResponse();
        GetGoingToExpiryProduct getGoingToExpiryProduct = null;
        List<GetGoingToExpiryProduct> getGoingToExpiryProductList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_GOING_TO_EXPIRY_PRODUCT_AND_LOCATION)) {

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    getGoingToExpiryProduct = new GetGoingToExpiryProduct();
                    getGoingToExpiryProduct.setPurchaseId(resultSet.getString("rpurchaseid"));
                    getGoingToExpiryProduct.setLocationName(resultSet.getString("rlocationname"));
                    getGoingToExpiryProduct.setLocationId(resultSet.getInt("rlocationid"));
                    getGoingToExpiryProduct.setItemName(resultSet.getString("ritemname"));
                    getGoingToExpiryProduct.setItemId(resultSet.getInt("ritemid"));
                    getGoingToExpiryProduct.setItemCount(resultSet.getInt("ritemcount"));
                    getGoingToExpiryProduct.setExpiryDate(resultSet.getString("rexpirydate"));
                    getGoingToExpiryProductList.add(getGoingToExpiryProduct);
                }
            }
            generalResponse.setData(getGoingToExpiryProduct);
            generalResponse.setMsg("Success");
            generalResponse.setStatusCode(200);
            generalResponse.setRes(true);
        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Error occur while getting expired products");
            generalResponse.setMsg(e.getMessage());
        }

        return generalResponse;
    }

    @Override
    public GeneralResponse getNoOfaProductInLocation(int itemId, int locationId) {
        GeneralResponse generalResponse = new GeneralResponse();

       GetItemAvailability getItemAvailability = new GetItemAvailability();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_NO_OF_AVAILABLE_ITEMS)) {
            callableStatement.setInt(1, itemId);
            callableStatement.setInt(2, locationId);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                getItemAvailability.setRItemAvailability(resultSet.getString("ritemavailability"));
            }

            generalResponse.setData(getItemAvailability);
            generalResponse.setMsg("Success");
            generalResponse.setStatusCode(200);
            generalResponse.setRes(true);
        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Error occur while getting available item in specific location details");
            generalResponse.setMsg(e.getMessage());
        }

        return generalResponse;
    }
}
