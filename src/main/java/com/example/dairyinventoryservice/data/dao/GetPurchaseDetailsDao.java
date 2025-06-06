package com.example.dairyinventoryservice.data.dao;

import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.model.dto.response.UserDetailsResponseDto;

public interface GetPurchaseDetailsDao {

    public GeneralResponse getUserDetails(String EmailId);

    public UserDetailsResponseDto getUserImportantDetails(String EmailId);

    public GeneralResponse getItemDetails();

    public GeneralResponse getLocationDetails();

    public GeneralResponse getExpiredAndWillExpireProducts();

    public GeneralResponse getNoOfaProductInLocation(int itemId,int locationId);

    public GeneralResponse getPurchaseDetails(String startDate, String endDate);

    public GeneralResponse getPurchaseDetailsByLocation(String startDate, String endDate, int locationId);

    public GeneralResponse getRevenueDetail(String startDate, String endDate);

    public GeneralResponse getRevenueDetailsByLocation(String startDate, String endDate, int locationId);

}
