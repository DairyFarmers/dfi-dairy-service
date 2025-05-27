package com.example.dairyinventoryservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesRepRevenueDto {
    String CreatedOn;
    String itemName;
    int inventoryItemCount;
    int soldItemCount;
    BigDecimal revenue;
    BigDecimal profit;
}
