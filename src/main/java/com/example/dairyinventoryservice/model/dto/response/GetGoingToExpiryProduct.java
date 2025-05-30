package com.example.dairyinventoryservice.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetGoingToExpiryProduct {
    private String purchaseId;
    private String locationName;
    private int locationId;
    private String itemName;
    private int itemId;
    private int itemCount;
    private String expiryDate;
}
