package com.example.dairyinventoryservice.controller;

import com.example.dairyinventoryservice.dto.request.*;
import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.service.GetPurchaseDetailsService;
import com.example.dairyinventoryservice.service.PostPurchaseDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private PostPurchaseDetailsService insertPurchaseDetails;

    @Autowired
    private GetPurchaseDetailsService getPurchaseDetails;

    @GetMapping("/getItemDetails")
    public ResponseEntity<GeneralResponse> getItemDetails() {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getItemDetails();
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @GetMapping("/getLocationDetails")
    public ResponseEntity<GeneralResponse> getLocationDetails() {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getLocationDetails();
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @GetMapping("/getExpiryProduct")
    public ResponseEntity<GeneralResponse> getExpiryProduct() {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getExpiredAndWillExpireProducts();
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @GetMapping("/userDetails")
    public ResponseEntity<GeneralResponse> userDetails(String emailId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getUserDetails(emailId);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @GetMapping("/purchaseDetails")
    public ResponseEntity<GeneralResponse> purchaseDetails(String startDate, String endDate) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getPurchaseDetails(startDate,endDate);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @GetMapping("/getProductCountByLocation")
    public ResponseEntity<GeneralResponse> getProductCountByLocation(int itemId,int locationId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getNoOfaProductInLocation(itemId,locationId);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/insertPurchase")
    public ResponseEntity<GeneralResponse> insertPurchaseDetail(@RequestBody PurchaseDetailDto insertPurchase){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addPurchaseDetails(insertPurchase);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/insertNewUser")
    public ResponseEntity<GeneralResponse> insertNewUser(@RequestBody InsertUserDto insertUserDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addNewUser(insertUserDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/updateUser")
    public ResponseEntity<GeneralResponse> updateUser(@RequestBody UpdateUserDto updateUserDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.updateUser(updateUserDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/addLocation")
    public ResponseEntity<GeneralResponse> addLocation(@RequestBody LocationDto locationDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addLocation(locationDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/addUserRole")
    public ResponseEntity<GeneralResponse> addUserRole(@RequestBody UserRoleDto userRoleDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addUserRole(userRoleDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/addItem")
    public ResponseEntity<GeneralResponse> addItem(@RequestBody ItemsDetailsDto itemDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addItem(itemDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/addSales")
    public ResponseEntity<GeneralResponse> addNewSalesDetails(@RequestBody SalesDetailsDto insertSalesDetails){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addNewSalesDetails(insertSalesDetails);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<GeneralResponse> changePassword(@RequestBody PasswordChangeDto passwordChangeDto){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.changePassword(passwordChangeDto);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

}