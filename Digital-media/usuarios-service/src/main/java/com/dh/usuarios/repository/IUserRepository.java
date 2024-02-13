package com.dh.usuarios.repository;

import com.dh.usuarios.model.User;
import com.dh.usuarios.model.UserKeycloak;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

  User validateAndGetUser(String username);

  Optional<User> getUserExtra(String username);

  User saveUserExtra(User userExtra);

  Optional<UserKeycloak> findByUserName(String userName);

  public List<UserKeycloak> findAll();

  List<UserKeycloak> findAllNonAdmin();

  Optional<UserKeycloak> findById(String id);
}
