package com.example.dairyinventoryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDetails {
    private String fullName;
    private String mobileNumber;
    private String UserRole;
    private int userRoleId;
    private String locationName;
}
