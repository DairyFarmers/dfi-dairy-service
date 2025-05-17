package com.example.dairyinventoryservice.controller;

import com.example.dairyinventoryservice.dto.request.*;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.service.InsertPurchaseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private InsertPurchaseDetailsService insertPurchaseDetails;

    @PostMapping("/insertPurchase")
    public ResponseEntity<GeneralResponse> insertPurchaseDetail(PurchaseDetailDto insertPurchase){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addPurchaseDetails(insertPurchase);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @PostMapping("/insertNewUser")
    public ResponseEntity<GeneralResponse> insertNewUser(InsertUserDto insertUserDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addNewUser(insertUserDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<GeneralResponse> updateUser(UpdateUserDto updateUserDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.updateUser(updateUserDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @PostMapping("/addLocation")
    public ResponseEntity<GeneralResponse> addLocation(LocationDto locationDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addLocation(locationDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @PostMapping("/addUserRole")
    public ResponseEntity<GeneralResponse> addUserRole(UserRoleDto userRoleDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addUserRole(userRoleDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @PostMapping("/addItem")
    public ResponseEntity<GeneralResponse> addItem(ItemDto itemDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addItem(itemDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

}
