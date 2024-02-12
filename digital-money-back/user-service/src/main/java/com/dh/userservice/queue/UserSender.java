package com.dh.userservice.queue;

import com.dh.userservice.domain.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSender {

    private final RabbitTemplate rabbitTemplate;

    private final Queue userQueue;

    public void sendUser(UserResponseDto user){
        this.rabbitTemplate.convertAndSend(this.userQueue.getName(), user);
    }

}
