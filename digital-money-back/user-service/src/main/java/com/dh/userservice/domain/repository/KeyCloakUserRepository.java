package com.dh.userservice.domain.repository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dh.userservice.domain.dto.request.UserRequestDto;
import com.dh.userservice.domain.dto.request.UserRequestLoginDto;
import com.dh.userservice.domain.dto.request.UserRequestUpdateUserInfoDto;
import com.dh.userservice.domain.dto.response.UserResponseCompleteDto;
import com.dh.userservice.domain.dto.response.UserResponseLoginDto;
import com.dh.userservice.domain.dto.response.UserResponseDto;
import com.dh.userservice.domain.dto.response.UserResponseLogoutDto;
import com.dh.userservice.domain.mapper.UserMapper;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.*;

@Repository
public class KeyCloakUserRepository implements IUserRepository {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Keycloak keycloak;

    @Value("${us.keycloak.server.realm}")
    private String realm;

    @Autowired
    private HttpServletRequest request;


    @Override
    public List<UserResponseDto> findNotAdmin() {

        List<UserRepresentation> usersRepresentation = keycloak.realm(realm).users().list();

        List<UserResponseDto> usersResponse = new ArrayList<>();

        for (UserRepresentation userRepresentation : usersRepresentation ) {
            List<GroupRepresentation> groupsRepresentation = keycloak.realm(realm).users().get(userRepresentation.getId()).groups();
            List<String> groups = new ArrayList<>();
            for (GroupRepresentation groupRepresentation : groupsRepresentation) {
                groups.add(groupRepresentation.getName());
            }

            if (!groups.contains("admin")){
                    usersResponse.add(userMapper.toUserResponseDto(userRepresentation));
            }
        }
        return usersResponse;
    }

    @Override
    public Optional<UserResponseDto> findById(String id) {
        List<UserRepresentation> users = keycloak.realm(realm).users().list();

        for (UserRepresentation user : users) {
            if (user.getId().equals(id)) {
                return Optional.of(userMapper.toUserResponseDto(user));
            }
        }
        return Optional.empty();
    }

    @Override
    public UserResponseLoginDto findByUserName(String userName) {
        UserRepresentation userRepresentation = keycloak.realm(realm).users().search(userName).get(0);

        return userMapper.userRespresentationToUserResponseDetailDto(userRepresentation);
    }

    @Override
    public Optional<UserResponseDto> create(UserRequestDto userRequestDto) {

        if(existsByUsername(userRequestDto.getEmail())){
            return Optional.empty();
        }

        UserRepresentation userRepresentation = userMapper.userRequestDtoToUserRespresentation(userRequestDto);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Map map = new HashMap<>();
        List<String> dni = new ArrayList<>();
        dni.add(userRequestDto.getDni());
        List<String> phone = new ArrayList<>();
        phone.add(userRequestDto.getPhone());
        map.put("dni", dni);
        map.put("phone", phone);
        userRepresentation.setAttributes(map);

        // Crear un objeto CredentialRepresentation con la contraseña del usuario
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userRequestDto.getPassword());

        userRepresentation.setCredentials(Arrays.asList(credential));

        Response response = keycloak.realm(realm).users().create(userRepresentation);

        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        for (UserRepresentation user : users) {
            if (userRepresentation.getEmail().equals(user.getEmail())) {
                userRepresentation = user;
            }
        }

        return Optional.of(userMapper.toUserResponseDto(userRepresentation));
    }


    @Override
    public Optional<UserResponseLoginDto> login(UserRequestLoginDto userRequestLoginDto) {

        if(!existsByUsername(userRequestLoginDto.getEmail())){
            return Optional.empty();
        }

        UserRepresentation userRepresentation = new UserRepresentation();

        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        for (UserRepresentation user : users) {
            if (userRequestLoginDto.getEmail().equals(user.getEmail())) {
                userRepresentation = user;
            }
        }

        String KEYCLOAK_BASE_URL = "http://34.201.108.135:8082/";
        String REALM = "Digital-Money";
        String CLIENT_ID = "microservices";
        String CLIENT_SECRET = "XLrXWsAejjeybqs48Zr0qiFVCQyse4Db";

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK_BASE_URL)
                .realm(REALM)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .username(userRepresentation.getUsername())
                .password(userRequestLoginDto.getPassword())
                .grantType("password")
                .build();

        Optional<AccessTokenResponse> tokenResponse = Optional.of(keycloak.tokenManager().getAccessToken());

        UserResponseLoginDto userResponseLoginDto = new UserResponseLoginDto();
        userResponseLoginDto.setToken(tokenResponse.get().getToken());

        if (tokenResponse != null) {
            return Optional.of(userResponseLoginDto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserResponseDto> update(UserRequestUpdateUserInfoDto userRequestUpdateUserInfoDto, String id) {

        if(!existsById(id)){
            return Optional.empty();
        }

        UserRepresentation userRepresentation = userMapper.userRequestUpdateUserInfoDtoToUserRespresentation(userRequestUpdateUserInfoDto);

        Map map = new HashMap<>();
        if(userRequestUpdateUserInfoDto.getDni() != null){
            List<String> dni = new ArrayList<>();
            dni.add(userRequestUpdateUserInfoDto.getDni());
            map.put("dni", dni);
            userRepresentation.setAttributes(map);
        }
        if(userRequestUpdateUserInfoDto.getPhone() != null){
            List<String> phone = new ArrayList<>();
            phone.add(userRequestUpdateUserInfoDto.getPhone());
            map.put("phone", phone);
            userRepresentation.setAttributes(map);
        }

        keycloak.realm(realm).users().get(id).update(userRepresentation);

        return findById(id);
    }

    public UserResponseLogoutDto logout() {
        String token = extractTokenFromHeader(request.getHeader("Authorization"));
        if (token == null) {
            throw new IllegalArgumentException("Token not provided in the Authorization header.");
        }

        DecodedJWT jwt = JWT.decode(token);
        String s = jwt.getSubject();

        UserRepresentation userRepresentation = new UserRepresentation();

        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        for (UserRepresentation user : users) {
            if (s.equals(user.getId())) {
                userRepresentation = user;
            }
        }

        // Configurar el cliente de administración de Keycloak
        String KEYCLOAK_BASE_URL = "http://34.201.108.135:8082/";
        String REALM = "Digital-Money";
        String CLIENT_ID = "microservices";
        String CLIENT_SECRET = "XLrXWsAejjeybqs48Zr0qiFVCQyse4Db";

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK_BASE_URL)
                .realm(REALM)  // Reino maestro para la administración de usuarios
                .clientId(CLIENT_ID)  // Cliente administrativo
                .clientSecret(CLIENT_SECRET)
                .username(userRepresentation.getUsername())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();

        // Obtener el reino
        RealmResource realmResource = keycloak.realm(REALM);

        // Invalidar el token del usuario
        realmResource.users().get(s).logout();

        return new UserResponseLogoutDto("Usuario cerró sesión correctamente.");
    }

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        return;
    }

    @Override
    public boolean existsByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(realm).users().search(username);
        return !users.isEmpty();
    }

    public boolean existsById(String id) {
        List<UserRepresentation> users = keycloak.realm(realm).users().list();

        for (UserRepresentation user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
