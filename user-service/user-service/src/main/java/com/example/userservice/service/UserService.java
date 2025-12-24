package com.example.userservice.service;

import com.example.userservice.dto.request.SignUpRequestDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(SignUpRequestDto request) {
        User user = User.create(request.getEmail(), request.getName(), request.getPassword());
        userRepository.save(user);
    }
}
