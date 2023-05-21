package com.dh.msusers.controller;

import com.dh.msusers.model.User;
import com.dh.msusers.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/firstname/{firstName}")
    public List<User> findByFirstName(@PathVariable String firstName) {
        return userService.findByFirstName(firstName);
    }
    @GetMapping("/id/{id}")
    public User findById(@PathVariable String id) {
        return userService.findById(id);
    }
    @PutMapping("/update")
    public User updateNationality(@RequestParam String id, @RequestParam String nationality) {
        return userService.updateNationality(id, nationality); }
}
