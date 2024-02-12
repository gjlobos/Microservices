package com.dh.transactionservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.ws.rs.HttpMethod;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new KeyCloakJwtAuthenticationConverter());
    http
            .requestMatchers()
            .antMatchers("/api/**")
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST , "/**"). access("#oauth2.hasScope('api') and hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.GET, "/transaction/getAll" ).authenticated()
            .anyRequest().authenticated();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri("http://34.201.108.135:8082/realms/Digital-Money/protocol/openid-connect/certs").build();
  }
}
