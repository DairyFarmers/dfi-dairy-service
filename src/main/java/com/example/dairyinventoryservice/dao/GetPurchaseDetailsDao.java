package com.example.dairyinventoryservice.dao;

import com.example.dairyinventoryservice.dto.response.GeneralResponse;

public interface GetPurchaseDetailsDao {

    public GeneralResponse getUserDetails(String EmailId);


}
