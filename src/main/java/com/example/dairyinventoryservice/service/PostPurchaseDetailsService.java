package com.example.dairyinventoryservice.service;

import com.example.dairyinventoryservice.model.dto.request.*;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;


public interface PostPurchaseDetailsService {

    public GeneralResponse addNewUser(InsertUserDto insertUserDto);

    public GeneralResponse updateUser(UpdateUserDto updateUserDto);

    public GeneralResponse addLocation(LocationDto locationDto);

    public GeneralResponse addUserRole(UserRoleDto userRoleDto);

    public GeneralResponse addItem(ItemsDetailsDto itemDto);

    public GeneralResponse addPurchaseDetails(PurchaseDetailDto insertPurchase);

    public GeneralResponse addNewSalesDetails(SalesDetailsDto insertSalesDetails);

}
