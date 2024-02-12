package com.dh.userservice.api.service;

import com.dh.userservice.Exceptions.ResourceNotFoundException;
import com.dh.userservice.api.client.AccountServiceClient;
import com.dh.userservice.domain.dto.request.UserRequestDto;
import com.dh.userservice.domain.dto.request.UserRequestLoginDto;
import com.dh.userservice.domain.dto.request.AccountRequestUpdateAccountInfoDto;
import com.dh.userservice.domain.dto.request.UserRequestUpdateUserInfoDto;
import com.dh.userservice.domain.dto.response.*;
import com.dh.userservice.domain.mapper.UserMapper;
import com.dh.userservice.domain.model.Account;
import com.dh.userservice.domain.model.User;
import com.dh.userservice.domain.repository.IUserRepository;
import com.dh.userservice.domain.repository.KeyCloakUserRepository;
import com.dh.userservice.queue.UserSender;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.dh.userservice.domain.enums.EMessageCode.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final MessageSource messageSource;
    private final IUserRepository userRepository;
    private final AccountServiceClient accountServiceClient;
    private final UserMapper mapper;

    @Override
    public UserResponseCompleteDto findById(String id) throws ResourceNotFoundException {
        UserResponseDto userResponseDto = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(USER_NOT_EXIST.name(),
                        new Object[] { id }, Locale.getDefault())));

        AccountResponseDto accountResponseDto = accountServiceClient.get(userResponseDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(USER_NOT_EXIST.name(),
                        new Object[] { }, Locale.getDefault())));

        UserResponseCompleteDto userResponseCompleteDto = mapper.userResponseDtoToUserResponseCompleteDto(userResponseDto);
        if (true) {
            userResponseCompleteDto.setCvu(accountResponseDto.getCvu());
            userResponseCompleteDto.setAlias(accountResponseDto.getAlias());
        }
        return userResponseCompleteDto;
    }

    @Override
    public UserResponseCompleteDto create(UserRequestDto userRequestDto) throws ResourceNotFoundException {
        UserResponseDto userResponseDto = userRepository.create(userRequestDto).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(EMAIL_EXIST.name(),
                        new Object[] { userRequestDto.getEmail() }, Locale.getDefault())));

        ResponseEntity<Account> accountResponseEntity = accountServiceClient.save(userResponseDto.getId());

        UserResponseCompleteDto userResponseCompleteDto = mapper.userResponseDtoToUserResponseCompleteDto(userResponseDto);
        if (accountResponseEntity.getStatusCode().is2xxSuccessful()) {
                userResponseCompleteDto.setCvu(accountResponseEntity.getBody().getCvu());
                userResponseCompleteDto.setAlias(accountResponseEntity.getBody().getAlias());
            }
        return userResponseCompleteDto;
    }

    @Override
    public UserResponseLoginDto login(UserRequestLoginDto userRequestLoginDto) throws ResourceNotFoundException {
        return  userRepository.login(userRequestLoginDto).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(USER_OR_PASSWORD_INCORRECT.name(),
                        new Object[] {}, Locale.getDefault())));
    }

    @Override
    public UserResponseCompleteDto updateUserInfo(UserRequestUpdateUserInfoDto userRequestUpdateUserInfoDto, String id) throws ResourceNotFoundException {
        UserResponseDto userResponseDto =  userRepository.update(userRequestUpdateUserInfoDto, id).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(USER_NOT_EXIST.name(),
                        new Object[] { id }, Locale.getDefault())));

        AccountResponseDto accountResponseDto = accountServiceClient.get(userResponseDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(USER_NOT_EXIST.name(),
                        new Object[] { }, Locale.getDefault())));

        UserResponseCompleteDto userResponseCompleteDto = mapper.userResponseDtoToUserResponseCompleteDto(userResponseDto);
        if (true) {
            userResponseCompleteDto.setCvu(accountResponseDto.getCvu());
            userResponseCompleteDto.setAlias(accountResponseDto.getAlias());
        }
        return userResponseCompleteDto;
    }

    @Override
    public UserResponseCompleteDto updateAccountInfo (AccountRequestUpdateAccountInfoDto accountRequestUpdateAccountInfoDto, String userId) throws ResourceNotFoundException {

        String alias = accountRequestUpdateAccountInfoDto.getAlias();

        int words = 0;
        for (int i = 0; i < alias.length(); i++) {
            if (alias.charAt(i) == '.') {
                words++;
            }
        }

        if(words > 2){
            throw new ResourceNotFoundException(messageSource.getMessage(ALIAS_FORMAT_INCORRECT.name(),
                    new Object[] { }, Locale.getDefault()));
        }

        UserResponseDto userResponseDto = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(USER_NOT_EXIST.name(),
                        new Object[] { userId }, Locale.getDefault())));

        AccountResponseDto accountResponseDto = accountServiceClient.get(userId).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(ACOUNT_NOT_FOUND_BY_ID.name(),
                        new Object[] { 1 }, Locale.getDefault())));

        accountResponseDto = accountServiceClient.updateByUserId(accountRequestUpdateAccountInfoDto, userId).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(ALIAS_EXIST.name(),
                        new Object[] { }, Locale.getDefault())));

        UserResponseCompleteDto userResponseCompleteDto = mapper.userResponseDtoToUserResponseCompleteDto(userResponseDto);
        userResponseCompleteDto.setCvu(accountResponseDto.getCvu());
        userResponseCompleteDto.setAlias(accountResponseDto.getAlias());

        return userResponseCompleteDto;
    }

    public Optional<String> findNameById(String id) throws ResourceNotFoundException {
        //UserResponseDto userResponseDto = userRepository.findById(id).orElseThrow(
        //       () -> new ResourceNotFoundException(messageSource.getMessage(USER_NOT_EXIST.name(),
        //               new Object[] { id }, Locale.getDefault())));

        Optional<UserResponseDto> userResponseDto = userRepository.findById(id);

        if (userResponseDto.isPresent()){
            String name = userResponseDto.get().getFirstName() + " " + userResponseDto.get().getLastName();
            return Optional.of(name);
        }

        return Optional.empty();
    }

    public UserResponseLogoutDto logout(){
        return  userRepository.logout();
    }

}
