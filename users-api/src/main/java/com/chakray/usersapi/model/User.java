package com.chakray.usersapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String password;
    private String taxId;
    private LocalDateTime createdAt;
    private List<Address> addresses;

}
