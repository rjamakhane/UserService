package com.example.userservice.dtos;

import com.example.userservice.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private Role role;
}
