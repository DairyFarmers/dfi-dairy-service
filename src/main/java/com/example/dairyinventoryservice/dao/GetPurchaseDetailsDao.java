package com.example.dairyinventoryservice.dao;

import com.example.dairyinventoryservice.dto.response.GeneralResponse;

public interface GetPurchaseDetailsDao {

    public GeneralResponse getUserDetails(String EmailId); /*Need to change to respond with user id also*/

    public GeneralResponse getPurchaseDetails(String startDate, String endDate);

    public GeneralResponse getItemDetails();

    public GeneralResponse getLocationDetails();

    public GeneralResponse getExpiredAndWillExpireProducts();

    public GeneralResponse getNoOfaProductInLocation(int itemId,int locationId);

}
