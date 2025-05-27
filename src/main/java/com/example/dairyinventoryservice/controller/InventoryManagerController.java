package com.example.dairyinventoryservice.controller;

import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.service.GetPurchaseDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/inventory")
public class InventoryManagerController {

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

    @GetMapping("/getProductCountByLocation")
    public ResponseEntity<GeneralResponse> getProductCountByLocation(int itemId, int locationId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getNoOfaProductInLocation(itemId, locationId);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @GetMapping("/purchaseDetails")
    public ResponseEntity<GeneralResponse> purchaseDetails(String startDate, String endDate) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getPurchaseDetails(startDate, endDate);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

    @GetMapping("/revenueDetails")
    public ResponseEntity<GeneralResponse> revenueDetails(String startDate, String endDate) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getRevenueDetail(startDate, endDate);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

}