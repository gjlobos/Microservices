package com.dh.userservice.api.controller;

import com.dh.userservice.api.service.UserService;
import com.dh.userservice.domain.dto.request.UserRequestDto;
import com.dh.userservice.domain.dto.request.UserRequestLoginDto;
import com.dh.userservice.domain.dto.request.AccountRequestUpdateAccountInfoDto;
import com.dh.userservice.domain.dto.request.UserRequestUpdateUserInfoDto;
import com.dh.userservice.domain.dto.response.UserResponseLoginDto;
import com.dh.userservice.domain.dto.response.UserResponseLogoutDto;
import com.dh.userservice.domain.repository.KeyCloakUserRepository;

import com.dh.userservice.shared.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.dh.userservice.shared.ResponseBuilder.*;
import static org.springframework.http.HttpStatus.*;


@RefreshScope
@RestController
public class UserController {

    private UserService userService;
    private RestTemplate restTemplate;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto userRequestDto) {
        try{
            return responseBuilder(CREATED, userService.create(userRequestDto));
        } catch (Exception e) {
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(NOT_FOUND, error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestLoginDto userRequestLoginDto) {
        try{
            return responseBuilder(CREATED, userService.login(userRequestLoginDto));
        } catch (Exception e) {
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(NOT_FOUND, error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try{
            return responseBuilder(CREATED, userService.findById(id));
        } catch (Exception e) {
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(NOT_FOUND, error);
        }
    }

    @GetMapping("/name/{id}")
    public String findNameById(@PathVariable String id){
        try{
            return userService.findNameById(id).get();
        } catch (Exception e) {
            ErrorDetails error = new ErrorDetails(e);
            return null;
        }
    }

    @Autowired
    private KeyCloakUserRepository userRepository;

    @PostMapping("/logout")
    public ResponseEntity<UserResponseLogoutDto> logoutUser() {
        return ResponseEntity.ok(userService.logout());
        /*try {
            userService.logout();
            UserResponseLogoutDto responseDto = new UserResponseLogoutDto("Usuario cerró sesión correctamente.");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            UserResponseLogoutDto responseDto = new UserResponseLogoutDto("Error al cerrar sesión del usuario.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }*/
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserRequestUpdateUserInfoDto userRequestUpdateUserInfoDto, @PathVariable String id){
        try{
            return responseBuilder(OK, userService.updateUserInfo(userRequestUpdateUserInfoDto, id));
        } catch (Exception e) {
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(NOT_FOUND, error);
        }
    }

    @PatchMapping("account/{id}")
    public ResponseEntity<?> updateAccountInfo(@RequestBody AccountRequestUpdateAccountInfoDto userRequestUpdateAccountInfoDto, @PathVariable String id){
        try{
            return responseBuilder(OK, userService.updateAccountInfo(userRequestUpdateAccountInfoDto, id));
        } catch (Exception e) {
            ErrorDetails error = new ErrorDetails(e);
            return responseBuilder(NOT_FOUND, error);
        }
    }
}
