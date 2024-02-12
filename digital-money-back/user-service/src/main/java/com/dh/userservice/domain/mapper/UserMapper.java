package com.dh.userservice.domain.mapper;


import com.dh.userservice.domain.dto.request.UserRequestDto;
import com.dh.userservice.domain.dto.request.UserRequestUpdateUserInfoDto;
import com.dh.userservice.domain.dto.response.UserResponseCompleteDto;
import com.dh.userservice.domain.dto.response.UserResponseLoginDto;
import com.dh.userservice.domain.dto.response.UserResponseDto;
import com.dh.userservice.domain.model.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserMapper {

    @Mapping(target= "dni", expression = "java(userRepresentation.getAttributes().get(\"dni\").get(0))")
    @Mapping(target= "phone", expression = "java(userRepresentation.getAttributes().get(\"phone\").get(0))")
    public abstract UserResponseDto toUserResponseDto(UserRepresentation userRepresentation);

    @Mapping(target= "dni", expression = "java(userRepresentation.getAttributes().get(\"dni\").get(0))")
    @Mapping(target= "phone", expression = "java(userRepresentation.getAttributes().get(\"phone\").get(0))")
    public abstract User toUser(UserRepresentation userRepresentation);
    public abstract UserResponseLoginDto userRespresentationToUserResponseDetailDto(UserRepresentation userRepresentation);

    @Mapping(target= "email", source = "email")
    @Mapping(target= "username", source = "email")
    public abstract UserRepresentation userRequestDtoToUserRespresentation(UserRequestDto userRequestDto);

    public abstract UserResponseDto userToUserResponseDto(User user);

    public abstract UserResponseCompleteDto userResponseDtoToUserResponseCompleteDto(UserResponseDto user);

    public abstract UserRepresentation userRequestUpdateUserInfoDtoToUserRespresentation(UserRequestUpdateUserInfoDto userRequestUpdateUserInfoDto);

}