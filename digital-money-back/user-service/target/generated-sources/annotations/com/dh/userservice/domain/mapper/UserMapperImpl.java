package com.dh.userservice.domain.mapper;

import com.dh.userservice.domain.dto.request.UserRequestDto;
import com.dh.userservice.domain.dto.request.UserRequestUpdateUserInfoDto;
import com.dh.userservice.domain.dto.response.UserResponseCompleteDto;
import com.dh.userservice.domain.dto.response.UserResponseDto;
import com.dh.userservice.domain.dto.response.UserResponseLoginDto;
import com.dh.userservice.domain.model.User;
import javax.annotation.processing.Generated;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-12T13:04:03-0300",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240103-0614, environment: Java 17.0.9 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public UserResponseDto toUserResponseDto(UserRepresentation userRepresentation) {
        if ( userRepresentation == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setEmail( userRepresentation.getEmail() );
        userResponseDto.setFirstName( userRepresentation.getFirstName() );
        userResponseDto.setId( userRepresentation.getId() );
        userResponseDto.setLastName( userRepresentation.getLastName() );

        userResponseDto.setDni( userRepresentation.getAttributes().get("dni").get(0) );
        userResponseDto.setPhone( userRepresentation.getAttributes().get("phone").get(0) );

        return userResponseDto;
    }

    @Override
    public User toUser(UserRepresentation userRepresentation) {
        if ( userRepresentation == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userRepresentation.getEmail() );
        user.setFirstName( userRepresentation.getFirstName() );
        user.setId( userRepresentation.getId() );
        user.setLastName( userRepresentation.getLastName() );

        user.setDni( userRepresentation.getAttributes().get("dni").get(0) );
        user.setPhone( userRepresentation.getAttributes().get("phone").get(0) );

        return user;
    }

    @Override
    public UserResponseLoginDto userRespresentationToUserResponseDetailDto(UserRepresentation userRepresentation) {
        if ( userRepresentation == null ) {
            return null;
        }

        UserResponseLoginDto userResponseLoginDto = new UserResponseLoginDto();

        return userResponseLoginDto;
    }

    @Override
    public UserRepresentation userRequestDtoToUserRespresentation(UserRequestDto userRequestDto) {
        if ( userRequestDto == null ) {
            return null;
        }

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setEmail( userRequestDto.getEmail() );
        userRepresentation.setUsername( userRequestDto.getEmail() );
        userRepresentation.setFirstName( userRequestDto.getFirstName() );
        userRepresentation.setLastName( userRequestDto.getLastName() );

        return userRepresentation;
    }

    @Override
    public UserResponseDto userToUserResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setDni( user.getDni() );
        userResponseDto.setEmail( user.getEmail() );
        userResponseDto.setFirstName( user.getFirstName() );
        userResponseDto.setId( user.getId() );
        userResponseDto.setLastName( user.getLastName() );
        userResponseDto.setPhone( user.getPhone() );

        return userResponseDto;
    }

    @Override
    public UserResponseCompleteDto userResponseDtoToUserResponseCompleteDto(UserResponseDto user) {
        if ( user == null ) {
            return null;
        }

        UserResponseCompleteDto userResponseCompleteDto = new UserResponseCompleteDto();

        userResponseCompleteDto.setDni( user.getDni() );
        userResponseCompleteDto.setEmail( user.getEmail() );
        userResponseCompleteDto.setFirstName( user.getFirstName() );
        userResponseCompleteDto.setLastName( user.getLastName() );
        userResponseCompleteDto.setPhone( user.getPhone() );

        return userResponseCompleteDto;
    }

    @Override
    public UserRepresentation userRequestUpdateUserInfoDtoToUserRespresentation(UserRequestUpdateUserInfoDto userRequestUpdateUserInfoDto) {
        if ( userRequestUpdateUserInfoDto == null ) {
            return null;
        }

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setFirstName( userRequestUpdateUserInfoDto.getFirstName() );
        userRepresentation.setLastName( userRequestUpdateUserInfoDto.getLastName() );

        return userRepresentation;
    }
}
