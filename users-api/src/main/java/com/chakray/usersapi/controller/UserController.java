package com.chakray.usersapi.controller;

//Importar modelo y funcionalidad de usuario
import com.chakray.usersapi.model.User;
import com.chakray.usersapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(
            @RequestParam(required = false) String sortedBy,
            @RequestParam(required = false) String filter
    ) {
        return userService.getUsers(sortedBy, filter);
    }
}
