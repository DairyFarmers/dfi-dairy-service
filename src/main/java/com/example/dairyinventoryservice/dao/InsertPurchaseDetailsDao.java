package com.example.dairyinventoryservice.dao;

import com.example.dairyinventoryservice.dto.request.*;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;

public interface InsertPurchaseDetailsDao {

    public GeneralResponse addNewUser(InsertUserDto insertUserDto);

    public GeneralResponse updateUser(UpdateUserDto updateUserDto);

    public GeneralResponse addLocation(LocationDto locationDto);

    public GeneralResponse addUserRole(UserRoleDto userRoleDto);

    public GeneralResponse addItem(ItemDto itemDto);

    public GeneralResponse addNewPurchaseDetails(PurchaseDetailDto insertPurchase);

}
