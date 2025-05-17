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
public class PurchaseDetailDto {
    int insertedBy;
    BigDecimal purchasePrice;
    int itemId;
    int sellerId;
    int itemCount;
    int inventoryLocationId;
}
