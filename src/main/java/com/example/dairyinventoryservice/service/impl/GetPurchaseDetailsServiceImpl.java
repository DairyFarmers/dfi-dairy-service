package com.example.dairyinventoryservice.service.impl;

import com.example.dairyinventoryservice.data.dao.GetPurchaseDetailsDao;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.service.GetPurchaseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPurchaseDetailsServiceImpl implements GetPurchaseDetailsService {

    @Autowired
    private GetPurchaseDetailsDao getPurchaseDetailsDao;

    @Override
    public GeneralResponse getUserDetails(String emailId) {
        return getPurchaseDetailsDao.getUserDetails(emailId);
    }

    @Override
    public GeneralResponse getPurchaseDetails(String startDate, String endDate) {
        return getPurchaseDetailsDao.getPurchaseDetails(startDate, endDate);
    }

    @Override
    public GeneralResponse getPurchaseDetailsByLocation(String startDate, String endDate, int locationId) {
        return getPurchaseDetailsDao.getPurchaseDetailsByLocation(startDate, endDate, locationId);
    }

    @Override
    public GeneralResponse getItemDetails() {
        return getPurchaseDetailsDao.getItemDetails();
    }

    @Override
    public GeneralResponse getLocationDetails() {
        return getPurchaseDetailsDao.getLocationDetails();
    }

    @Override
    public GeneralResponse getExpiredAndWillExpireProducts() {
        return getPurchaseDetailsDao.getExpiredAndWillExpireProducts();
    }

    @Override
    public GeneralResponse getNoOfaProductInLocation(int itemId, int locationId) {
        return getPurchaseDetailsDao.getNoOfaProductInLocation(itemId, locationId);
    }

    @Override
    public GeneralResponse getRevenueDetail(String startDate, String endDate) {
        return getPurchaseDetailsDao.getRevenueDetail(startDate, endDate);
    }


    @Override
    public GeneralResponse getRevenueDetailsByLocation(String startDate, String endDate, int locationId) {
        return getPurchaseDetailsDao.getRevenueDetailsByLocation(startDate, endDate, locationId);
    }

}
