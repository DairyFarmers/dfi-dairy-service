package com.example.dairyinventoryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse{
    Object data = null;
    Boolean res = false;
    String msg = "Error in while fetching data";
    Integer statusCode = 400;
}
