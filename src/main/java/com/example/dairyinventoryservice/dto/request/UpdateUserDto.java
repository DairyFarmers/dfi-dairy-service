package com.example.dairyinventoryservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String email;
    private String mobileNumber;
    private String firstName;
    private String lastName;
    private int userRoleId;
    private int locationID;
}
