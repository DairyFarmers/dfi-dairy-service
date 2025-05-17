package com.example.dairyinventoryservice.service;

import com.example.dairyinventoryservice.dto.response.GeneralResponse;

public interface GetPurchaseDetailsService {
    public GeneralResponse getUserDetails(String emailId);
}
