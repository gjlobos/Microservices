package com.dh.userservice.api.service;

import com.dh.userservice.Exceptions.ResourceNotFoundException;
import com.dh.userservice.domain.dto.request.AccountRequestUpdateAccountInfoDto;
import com.dh.userservice.domain.dto.request.UserRequestDto;
import com.dh.userservice.domain.dto.request.UserRequestLoginDto;
import com.dh.userservice.domain.dto.request.UserRequestUpdateUserInfoDto;
import com.dh.userservice.domain.dto.response.ApiResponseDto;
import com.dh.userservice.domain.dto.response.UserResponseCompleteDto;
import com.dh.userservice.domain.dto.response.UserResponseDto;
import com.dh.userservice.domain.dto.response.UserResponseLoginDto;
import com.dh.userservice.domain.model.User;
import com.dh.userservice.shared.GenericServiceAPI;

public interface IUserService {

    UserResponseCompleteDto findById(String id) throws ResourceNotFoundException;

    UserResponseCompleteDto create(UserRequestDto userRequestDto) throws ResourceNotFoundException;

    UserResponseLoginDto login(UserRequestLoginDto userRequestLoginDto) throws ResourceNotFoundException;

    UserResponseCompleteDto updateUserInfo(UserRequestUpdateUserInfoDto userRequestUpdateUserInfoDto, String id) throws ResourceNotFoundException;

    UserResponseCompleteDto updateAccountInfo (AccountRequestUpdateAccountInfoDto accountRequestUpdateAccountInfoDto, String userId) throws ResourceNotFoundException;

}
