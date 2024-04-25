package com.example.auth.controllers;

import com.example.auth.domain.dtos.user.role.UserRoleDTO;
import com.example.auth.domain.dtos.user.role.UserRoleModificationDTO;
import com.example.auth.domain.services.interfaces.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/userRole")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRoleDTO registerUserRole(@RequestBody UserRoleModificationDTO userRoleData) {
        return userRoleService.register(userRoleData);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserRoleDTO updateUserRole(@PathVariable Long id, @RequestBody UserRoleModificationDTO userRoleData) {
        return userRoleService.update(id, userRoleData);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserRoleDTO getUserRole(@PathVariable Long id) {
        return userRoleService.get(id);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRoleDTO> getUserRoleList() {
        return userRoleService.getList();
    }
}
