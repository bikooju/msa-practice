package com.example.boardservice.service;

import com.example.boardservice.dto.request.SaveUserRequestDto;
import com.example.boardservice.entity.User;
import com.example.boardservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void save(SaveUserRequestDto saveUserRequestDto) {
    User user = new User(
        saveUserRequestDto.getUserId(),
        saveUserRequestDto.getName()
    );

    userRepository.save(user);
  }

}
