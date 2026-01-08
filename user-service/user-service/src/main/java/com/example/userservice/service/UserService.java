package com.example.userservice.service;

import com.example.userservice.client.PointClient;
import com.example.userservice.dto.request.AddActivityScoreRequestDto;
import com.example.userservice.dto.request.SignUpRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointClient pointClient;

    @Transactional
    public void signup(SignUpRequestDto request) {
        User user = User.create(request.getEmail(), request.getName(), request.getPassword(), 0);
        User savedUser = userRepository.save(user);

        // 회원가입하면 포인트 1000점 적립
        pointClient.addPoints(savedUser.getUserId(), 1000);
    }

    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(("사용자를 찾을 수 없습니다.")));

        return new UserResponseDto(
            user.getUserId(),
            user.getEmail(),
            user.getName()
        );
    }

    public List<UserResponseDto> getUsersByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);

        return users.stream()
            .map(user -> new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getName()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public void addActivityScore(
        AddActivityScoreRequestDto addActivityScoreRequestDto
    ) {
        User user = userRepository.findById(addActivityScoreRequestDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException(("사용자를 찾을 수 없습니다.")));

        user.addActivityScore(addActivityScoreRequestDto.getScore());

        userRepository.save(user);
        throw new RuntimeException("에러 발생"); // 보상 트랜잭션 테스트 하기 위해서 예외 던짐
    }
}
