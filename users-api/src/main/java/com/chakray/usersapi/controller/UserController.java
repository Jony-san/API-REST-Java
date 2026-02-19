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
//Login-dto
import com.chakray.usersapi.dto.LoginRequest;
//Update-dto
import com.chakray.usersapi.dto.UpdateUserRequest;
//Documentación
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;
import java.util.List;

@Tag(name = "Users API", description = "Operations related to users management")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
    summary = "Get users",
    description = """
        Returns a list of users with optional sorting and filtering.

        Query Parameters:

        • sortedBy:
            Allows sorting by one of the following fields:
            id, email, name, phone, tax_id, created_at

        • filter:
            Format: field+operator+value

            Supported operators:
            - co : contains
            - eq : equals
            - sw : starts with
            - ew : ends with

        Examples:
            /users?filter=name+co+user
            /users?filter=email+ew+mail.com
            /users?filter=phone+sw+555
            /users?filter=tax_id+eq+AARR990101XXX
        """
)
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

    @Operation(
    summary = "Create a new user",
    description = """
        Creates and stores a new user in memory.

        Considerations:
        
        • tax_id must be unique
        
        • tax_id must follow RFC format validation
        
        • phone must follow 10-digit AndresFormat validation
        
        • password is encrypted using AES-256 before storing
        
        • created_at is generated automatically using Madagascar time zone
        
        • password is not returned in the response
        
        """
)
    @PostMapping
    public UserResponseDTO createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        return UserMapper.toDTO(
                userService.createUser(request)
        );
    }

    @Operation(
    summary = "Authenticate user",
    description = """
        Authenticates a user using tax_id as username and password.

        • Password is validated against AES-256 encrypted value.

        • Returns success or invalid credentials message.
        """
    )
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
    
        boolean authenticated = userService.login(request);
    
        if (authenticated) {
            return "Login successful";
        }
    
        return "Invalid credentials";
    }

    @Operation(
    summary = "Update user by ID",
    description = """
        Updates one or more attributes of an existing user by UUID.

        • Only provided fields are updated.

        • User must exist.
        """
    )
    @PatchMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {
        return UserMapper.toDTO(
                userService.updateUser(id, request)
        );
    }

    /*
    Consideración: En producción no se recomienda eliminar usuarios sino dejarlos en un estado inactivo
    en caso de reactivación de usuarios
    */
    @Operation(
    summary = "Delete user by ID",
    description = """
        Removes a user from memory by UUID.

        Note:
        In production environments, soft-delete is recommended instead of permanent removal.
        """
    )
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable UUID id) {

        userService.deleteUser(id);

        return "User deleted successfully";
    }


}
