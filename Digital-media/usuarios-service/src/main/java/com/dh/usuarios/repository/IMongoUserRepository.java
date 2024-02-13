package com.dh.usuarios.repository;

import com.dh.usuarios.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IMongoUserRepository extends MongoRepository<User,String> {
}


