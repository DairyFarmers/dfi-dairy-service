package com.example.dairyinventoryservice.service;

import com.example.dairyinventoryservice.dto.response.GeneralResponse;

public interface GetPurchaseDetailsService {
    public GeneralResponse getUserDetails(String emailId);

    public GeneralResponse getPurchaseDetails(String startDate, String endDate);

    public GeneralResponse getItemDetails();

    public GeneralResponse getLocationDetails();

    public GeneralResponse getExpiredAndWillExpireProducts();

    public GeneralResponse getNoOfaProductInLocation(int itemId,int locationId);

    public GeneralResponse getPurchaseDetailsByLocation(String startDate, String endDate, int locationId);

    public GeneralResponse getRevenueDetail(String startDate, String endDate);

    public GeneralResponse getRevenueDetailsByLocation(String startDate, String endDate, int locationId);
}
