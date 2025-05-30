package com.example.dairyinventoryservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDetailsDto {
    private String itemName;
    private String itemExpiryDuration;
    private BigDecimal maxPurchaseItemPrice;
    private BigDecimal maxSellingItemPrice;
}
