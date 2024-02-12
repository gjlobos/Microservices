package com.dh.userservice.domain.repository;

import com.dh.userservice.domain.dto.request.UserRequestDto;
import com.dh.userservice.domain.dto.request.UserRequestLoginDto;
import com.dh.userservice.domain.dto.request.UserRequestLogoutDto;
import com.dh.userservice.domain.dto.request.UserRequestUpdateUserInfoDto;
import com.dh.userservice.domain.dto.response.UserResponseLoginDto;
import com.dh.userservice.domain.dto.response.UserResponseDto;
import com.dh.userservice.domain.dto.response.UserResponseLogoutDto;
import com.dh.userservice.domain.model.User;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository {

    Optional<UserResponseDto> findById(String id);

    Optional<UserResponseDto> create(UserRequestDto userRequestDto);

    Optional<UserResponseLoginDto> login(UserRequestLoginDto userRequestLoginDto);

    Optional<UserResponseDto> update(UserRequestUpdateUserInfoDto userRequestUpdateUserInfoDto, String id);



    //anteriores
    public List<UserResponseDto> findNotAdmin();

    //anteriores


    //anteriores
    public UserResponseLoginDto findByUserName(String name);

    public UserResponseLogoutDto logout();

    public void delete(Integer id);

    public boolean existsByUsername(String username);


}
