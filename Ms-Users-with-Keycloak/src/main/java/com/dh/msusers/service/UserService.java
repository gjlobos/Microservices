package com.dh.msusers.service;

import com.dh.msusers.model.User;
import com.dh.msusers.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public User findById(String id) {
        return userRepository.findById(id);
    }

    public User updateNationality(String id, String nationality) {
        return userRepository.updateNationality(id, nationality);
    }
}
