package com.dh.usuarios.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {
    @Value("${dh.keycloak.serverUrl}")
    private String serverUrl;
    @Value("${dh.keycloak.realm}")
    private String realm;
    @Value("${dh.keycloak.clientId}")
    private String clientId;

    @Bean
    public Keycloak buildClient(){
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .build();
    }

}
