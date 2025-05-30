package com.example.dairyinventoryservice.controller;

import com.example.dairyinventoryservice.model.dto.request.PurchaseDetailDto;
import com.example.dairyinventoryservice.model.dto.request.SalesDetailsDto;
import com.example.dairyinventoryservice.model.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.service.GetPurchaseDetailsService;
import com.example.dairyinventoryservice.service.PostPurchaseDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/shop")
public class SalesRepController {

    @Autowired
    private PostPurchaseDetailsService insertPurchaseDetails;

    @Autowired
    private GetPurchaseDetailsService getPurchaseDetails;

    @GetMapping("/userDetails")
    public ResponseEntity<GeneralResponse> userDetails(String emailId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getUserDetails(emailId);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

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

    @GetMapping("/getProductCountByLocation")
    public ResponseEntity<GeneralResponse> getProductCountByLocation(int itemId,int locationId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getNoOfaProductInLocation(itemId,locationId);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    //Add a function to find the purchase details by location
    @GetMapping("/purchaseDetails")
    public ResponseEntity<GeneralResponse> purchaseDetailsByLocation(String startDate, String endDate,int locationId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getPurchaseDetailsByLocation(startDate, endDate, locationId);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    // Add a function to find the profit from the location and sales details (Revenue)
    @GetMapping("/revenue")
    public ResponseEntity<GeneralResponse> salesDetailsByLocation(String startDate, String endDate,int locationId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getRevenueDetailsByLocation(startDate, endDate, locationId);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }


    @PostMapping("/insertPurchase")
    public ResponseEntity<GeneralResponse> insertPurchaseDetail(@RequestBody PurchaseDetailDto insertPurchase){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addPurchaseDetails(insertPurchase);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @PostMapping("/addSales")
    public ResponseEntity<GeneralResponse> addNewSalesDetails(@RequestBody SalesDetailsDto insertSalesDetails){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = insertPurchaseDetails.addNewSalesDetails(insertSalesDetails);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }


}