package com.example.dairyinventoryservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetItemDetailsDto {
    private int itemId;
    private String itemName;
    private String itemExpiryDuration;
}
