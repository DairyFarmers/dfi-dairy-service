package com.example.dairyinventoryservice.service.impl;

import com.example.dairyinventoryservice.dao.GetPurchaseDetailsDao;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.service.GetPurchaseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPurchaseDetailsServiceImpl implements GetPurchaseDetailsService {

    @Autowired
    private GetPurchaseDetailsDao getPurchaseDetailsDao;

    public GeneralResponse getUserDetails(String emailId){
        return getPurchaseDetailsDao.getUserDetails(emailId);
    }
}
