package com.chakray.usersapi.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String email;
    private String name;
    private String phone;
    private String password;
    private String taxId;
}
