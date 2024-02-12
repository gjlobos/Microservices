package com.dh.accountservice.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="user-service")
public interface UserServiceClient {

    @GetMapping("users/name/{id}")
    String findNameById(@PathVariable String id);
}
