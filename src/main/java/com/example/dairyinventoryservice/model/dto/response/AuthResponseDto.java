package com.example.dairyinventoryservice.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String token;
    private int roleId;
    private String fullName;
    private String email;
    private String userRoleName;
    private String locationName;
}
