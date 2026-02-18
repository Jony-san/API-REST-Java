package com.chakray.usersapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDTO {

    private Integer id;
    private String name;
    private String street;
    private String countryCode;
}
