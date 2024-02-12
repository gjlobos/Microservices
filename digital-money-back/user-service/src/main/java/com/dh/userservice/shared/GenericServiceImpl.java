package com.dh.userservice.shared;

import com.dh.userservice.domain.repository.KeyCloakUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public abstract class GenericServiceImpl <T, ID extends Serializable> implements GenericServiceAPI<T, ID> {

}

