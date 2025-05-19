package com.example.dairyinventoryservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesDetailsDto {
    int itemID;
    int itemCount;
    int inventoryLocationId;
    BigDecimal salesPrice;
    int sellerId;
    boolean isB2b;
}

/*
*    pItemId INT,
    pItemCount INT,
    pInventoryLocationId INT,
    pSalesPrice NUMERIC(10, 12),
    pSellerId integer,
    pIsb2b boolean
    * */