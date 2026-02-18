package com.chakray.usersapi.controller;

//Importar modelo y funcionalidad de usuario
import com.chakray.usersapi.model.User;
import com.chakray.usersapi.service.UserService;
import com.chakray.usersapi.dto.UserResponseDTO;
import com.chakray.usersapi.util.UserMapper;
import org.springframework.web.bind.annotation.*;
//importar metodos de usuario
import com.chakray.usersapi.dto.CreateUserRequest;
import jakarta.validation.Valid;
//Login
import com.chakray.usersapi.dto.LoginRequest;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getUsers(
        @RequestParam(required = false) String sortedBy,
        @RequestParam(required = false) String filter
) {
    return userService.getUsers(sortedBy, filter)
            .stream()
            .map(UserMapper::toDTO)
            .toList();
}

    @PostMapping
    public UserResponseDTO createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        return UserMapper.toDTO(
                userService.createUser(request)
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
    
        boolean authenticated = userService.login(request);
    
        if (authenticated) {
            return "Login successful";
        }
    
        return "Invalid credentials";
    }



}
