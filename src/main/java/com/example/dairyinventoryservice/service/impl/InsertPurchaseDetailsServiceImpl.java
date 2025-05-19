package com.example.dairyinventoryservice.service.impl;

import com.example.dairyinventoryservice.dao.InsertPurchaseDetailsDao;
import com.example.dairyinventoryservice.dto.request.*;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.service.InsertPurchaseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InsertPurchaseDetailsServiceImpl implements InsertPurchaseDetailsService {

    @Autowired
    private InsertPurchaseDetailsDao insertPurchaseDetailsDao;

    @Override
    public GeneralResponse addPurchaseDetails(PurchaseDetailDto insertPurchase){
        return insertPurchaseDetailsDao.addNewPurchaseDetails(insertPurchase);
    }

    @Override
    public GeneralResponse addNewUser(InsertUserDto insertUserDto){
        return insertPurchaseDetailsDao.addNewUser(insertUserDto);
    }

    @Override
    public GeneralResponse updateUser(UpdateUserDto updateUserDto){
        return insertPurchaseDetailsDao.updateUser(updateUserDto);
    }

    @Override
    public GeneralResponse addLocation(LocationDto locationDto){
        return insertPurchaseDetailsDao.addLocation(locationDto);
    }

    @Override
    public GeneralResponse addUserRole(UserRoleDto userRoleDto){
        return insertPurchaseDetailsDao.addUserRole(userRoleDto);
    }

    @Override
    public GeneralResponse addItem(ItemsDetailsDto itemDto){
        return insertPurchaseDetailsDao.addItem(itemDto);
    }

    @Override
    public GeneralResponse addNewSalesDetails(SalesDetailsDto insertSalesDetails){
        return insertPurchaseDetailsDao.addNewSalesDetails(insertSalesDetails);
    }

}
