package com.chakray.usersapi.dto;

import com.chakray.usersapi.dto.AddressDTO;

import lombok.Data;
import java.util.List;

@Data
public class UpdateUserRequest {

    private String email;
    private String name;
    private String phone;
    private String password;
    private String taxId;

    private List<AddressDTO> addresses;
    
}
