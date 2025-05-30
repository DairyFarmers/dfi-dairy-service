package com.example.dairyinventoryservice.controller;

import com.example.dairyinventoryservice.model.dto.request.*;
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
@RequestMapping("/admin")
public class AdminController {

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

    @GetMapping("/userDetails")
    public ResponseEntity<GeneralResponse> userDetails(String emailId) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = getPurchaseDetails.getUserDetails(emailId);
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

}