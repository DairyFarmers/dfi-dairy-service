package com.example.dairyinventoryservice.service;

import com.example.dairyinventoryservice.dto.response.GeneralResponse;

public interface GetPurchaseDetailsService {
    public GeneralResponse getUserDetails(String emailId);

    public GeneralResponse getPurchaseDetails(String startDate, String endDate);

    public GeneralResponse getItemDetails();

    public GeneralResponse getLocationDetails();

    public GeneralResponse getExpiredAndWillExpireProducts();

    public GeneralResponse getNoOfaProductInLocation(int itemId,int locationId);
}
