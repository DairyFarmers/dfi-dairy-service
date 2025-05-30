package com.example.dairyinventoryservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserDto {
    private String firstName;
    private String lastName;
    private int userRoleId;
    private String email;
    private String phoneNumber;
    private int locationId;
    private String password;
}
