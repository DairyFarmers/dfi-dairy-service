package com.example.dairyinventoryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRevenueDto {
    String CreatedOn;
    String itemName;
    int inventoryItemCount;
    int soldItemCount;
    String locationName;
    BigDecimal revenue;
    BigDecimal profit;
}
