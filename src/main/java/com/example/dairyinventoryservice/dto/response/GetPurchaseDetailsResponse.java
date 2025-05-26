package com.example.dairyinventoryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPurchaseDetailsResponse {
    private String rItemName;
    private int rItemCount;
    private String rCreatedOn;
    private String rExpiryDate;
    private String rInventoryLocationName;
    private String rCreatedBy;
    private String rSoldBy;
}
