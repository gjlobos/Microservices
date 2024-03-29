package com.facturacion.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class KeyCloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  
  private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
  private static final Logger LOG = LoggerFactory.getLogger(KeyCloakJwtAuthenticationConverter.class);


  private static Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) throws JsonProcessingException {
    Set<GrantedAuthority> resourcesRoles = new HashSet();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    resourcesRoles.addAll(extractRoles("resource_access", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    resourcesRoles.addAll(extractRoles("realm_access", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    resourcesRoles.addAll(extractScope("scope", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    resourcesRoles.addAll(extractGroups("groups", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    resourcesRoles.addAll(extractAud("aud", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    LOG.info("JWT: " + resourcesRoles);
    return resourcesRoles;
  }


  private static List<GrantedAuthority> extractRoles(String route, JsonNode jwt) {
    Set<String> rolesWithPrefix = new HashSet<>();
    jwt.path(route)
            .elements()
            .forEachRemaining(e -> e.path("roles")
                    .elements()
                    .forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText())));
    return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
  }

  private static List<GrantedAuthority> extractAud(String route, JsonNode jwt) {
    Set<String> rolesWithPrefix = new HashSet<>();
    jwt.path(route)
            .elements()
            .forEachRemaining(e ->rolesWithPrefix.add("AUD_" + e.asText()));
    return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
  }

  private static List<GrantedAuthority> extractScope(String route, JsonNode jwt) {
    Set<String> rolesWithPrefix = new HashSet<>();
    jwt.path(route)
            .elements()
            .forEachRemaining(e ->rolesWithPrefix.add("SCOPE_" + e.asText()));
    return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
  }

  private static List<GrantedAuthority> extractGroups(String route, JsonNode jwt) {
    Set<String> rolesWithPrefix = new HashSet<>();
    jwt.path(route)
            .elements()
            .forEachRemaining(e ->rolesWithPrefix.add("GROUP_" + e.asText().replace("/", "")));
    return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
  }

  public AbstractAuthenticationToken convert(final Jwt source) {
    Collection<GrantedAuthority> authorities = null;
    try {
      authorities = this.getGrantedAuthorities(source);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return new JwtAuthenticationToken(source, authorities);
  }

  public Collection<GrantedAuthority> getGrantedAuthorities(Jwt source) throws JsonProcessingException {
    return Stream.concat(this.defaultGrantedAuthoritiesConverter.convert(source).stream(),
            extractResourceRoles(source).stream()).collect(Collectors.toSet());
  }
}
