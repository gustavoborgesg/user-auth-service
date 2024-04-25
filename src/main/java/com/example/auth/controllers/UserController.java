package com.example.auth.controllers;

import com.example.auth.domain.dtos.user.UserDTO;
import com.example.auth.domain.dtos.user.UserGetDTO;
import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
import com.example.auth.domain.dtos.user.UserModificationDTO;
import com.example.auth.domain.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserModificationDTO userData) {
        return userService.update(id, userData);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long id) {
        return userService.get(id);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserGetDTO> getUserList(@ModelAttribute UserGetListFiltersDTO userFilters, Pageable pageable) {
        return userService.getList(userFilters, pageable);
    }
}
