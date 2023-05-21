package com.dh.msusers.repository;

import com.dh.msusers.model.User;

import java.util.List;

public interface IUserRepository {

    List<User> findByFirstName(String name);

    User findById(String id);

    User updateNationality(String id, String nationality);
}
