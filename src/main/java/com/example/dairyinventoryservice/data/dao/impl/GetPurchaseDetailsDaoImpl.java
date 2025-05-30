package com.example.dairyinventoryservice.data.dao.impl;

import com.example.dairyinventoryservice.data.dao.DaoConstant;
import com.example.dairyinventoryservice.data.dao.GetPurchaseDetailsDao;
import com.example.dairyinventoryservice.model.dto.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
                    getUserDetails.setUserID(resultSet.getInt("luserid"));
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
    public UserDetailsResponseDto getUserImportantDetails(String emailId){
        UserDetailsResponseDto userDetailsResponseDto = null;


        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_USER_DETAIL_BY_EMAIL_ID)) {

            callableStatement.setString(1, emailId);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {

                    userDetailsResponseDto = new UserDetailsResponseDto();

                    userDetailsResponseDto.setFullName(resultSet.getString("lfullname"));
                    userDetailsResponseDto.setUserRoleName(resultSet.getString("luserrole"));
                    userDetailsResponseDto.setLocationName(resultSet.getString("luserlocation"));
                    userDetailsResponseDto.setEmail(emailId);

            }

        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            return userDetailsResponseDto;

        }
        return userDetailsResponseDto;
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
    public GeneralResponse getPurchaseDetailsByLocation(String startDate, String endDate,int locationId) {
        GeneralResponse generalResponse = new GeneralResponse();
        GetPurchaseDetailsResponse getPurchaseDetailsResponse = null;
        List<GetPurchaseDetailsResponse> getPurchaseDetailsList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_PURCHASE_DETAILS_BY_DATE_RANGE)) {

            callableStatement.setString(1, startDate);
            callableStatement.setString(2, endDate);
            callableStatement.setInt(3, locationId);

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
                    getItemDetailsDto.setMaxPurchaseItemPrice(resultSet.getBigDecimal(4));
                    getItemDetailsDto.setMaxSellingItemPrice(resultSet.getBigDecimal(5));
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

    @Override
    public GeneralResponse getRevenueDetail(String startDate, String endDate){
        GeneralResponse generalResponse = new GeneralResponse();
        InventoryRevenueDto inventoryRevenueDto = null;
        List<InventoryRevenueDto>inventoryRevenueDtoList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_REVENUE_BY_DATE)) {

            callableStatement.setString(1,startDate);
            callableStatement.setString(2,endDate);

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    inventoryRevenueDto = new InventoryRevenueDto();
                    inventoryRevenueDto.setCreatedOn(resultSet.getString("lcreatedon"));
                    inventoryRevenueDto.setItemName(resultSet.getString("litemname"));
                    inventoryRevenueDto.setInventoryItemCount(resultSet.getInt("linventoryitemcount"));
                    inventoryRevenueDto.setSoldItemCount(resultSet.getInt("lsolditemcount"));
                    inventoryRevenueDto.setLocationName(resultSet.getString("llocationname"));
                    inventoryRevenueDto.setRevenue(resultSet.getBigDecimal("lrevenue"));
                    inventoryRevenueDto.setProfit(resultSet.getBigDecimal("lprofit"));
                    inventoryRevenueDtoList.add(inventoryRevenueDto);
                }
            }
            generalResponse.setData(inventoryRevenueDtoList);
            generalResponse.setMsg("Success");
            generalResponse.setStatusCode(200);
            generalResponse.setRes(true);
        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Error occur while getting entire revenue details");
            generalResponse.setMsg(e.getMessage());
        }

        return generalResponse;
    }

    @Override
    public GeneralResponse getRevenueDetailsByLocation(String startDate, String endDate, int locationId){
        GeneralResponse generalResponse = new GeneralResponse();
        SalesRepRevenueDto salesRepRevenueDto = null;
        List<SalesRepRevenueDto>salesRepRevenueDtoList = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
             CallableStatement callableStatement = connection.prepareCall(DaoConstant.GET_REVENUE_BY_DATE_AND_LOCATION)) {

            callableStatement.setString(1,startDate);
            callableStatement.setString(2,endDate);
            callableStatement.setInt(3,locationId);
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    salesRepRevenueDto = new SalesRepRevenueDto();
                    salesRepRevenueDto.setCreatedOn(resultSet.getString("lcreatedon"));
                    salesRepRevenueDto.setItemName(resultSet.getString("litemname"));
                    salesRepRevenueDto.setInventoryItemCount(resultSet.getInt("linventoryitemcount"));
                    salesRepRevenueDto.setSoldItemCount(resultSet.getInt("lsolditemcount"));
                    salesRepRevenueDto.setRevenue(resultSet.getBigDecimal("lrevenue"));
                    salesRepRevenueDto.setProfit(resultSet.getBigDecimal("lprofit"));
                    salesRepRevenueDtoList.add(salesRepRevenueDto);
                }
            }
            generalResponse.setData(salesRepRevenueDtoList);
            generalResponse.setMsg("Success");
            generalResponse.setStatusCode(200);
            generalResponse.setRes(true);
        } catch (SQLException e) {
            log.error("---------------------------------------------{}", e.getMessage());
            generalResponse.setData("Error occur while getting revenue details by location");
            generalResponse.setMsg(e.getMessage());
        }

        return generalResponse;
    }
}
