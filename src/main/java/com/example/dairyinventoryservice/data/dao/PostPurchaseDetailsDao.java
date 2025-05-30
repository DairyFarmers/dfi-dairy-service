package com.example.dairyinventoryservice.data.dao;

import com.example.dairyinventoryservice.model.dto.request.*;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;

public interface PostPurchaseDetailsDao {

    public GeneralResponse addNewUser(InsertUserDto insertUserDto);

    public GeneralResponse updateUser(UpdateUserDto updateUserDto);

    public GeneralResponse addLocation(LocationDto locationDto);

    public GeneralResponse addUserRole(UserRoleDto userRoleDto);

    public GeneralResponse addItem(ItemsDetailsDto itemDto);

    public GeneralResponse addNewPurchaseDetails(PurchaseDetailDto insertPurchase);

    public GeneralResponse addNewSalesDetails(SalesDetailsDto insertSalesDetails);



}
