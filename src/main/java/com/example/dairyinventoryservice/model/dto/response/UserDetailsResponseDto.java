package com.example.dairyinventoryservice.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseDto {
    private String fullName;
    private String email;
    private String userRoleName;
    private String locationName;
}
