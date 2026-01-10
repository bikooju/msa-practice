package com.example.userservice.service;

import com.example.userservice.client.PointClient;
import com.example.userservice.dto.request.AddActivityScoreRequestDto;
import com.example.userservice.dto.request.LoginRequestDto;
import com.example.userservice.dto.request.SignUpRequestDto;
import com.example.userservice.dto.response.LoginResponseDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.event.UserSignedUpEvent;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PointClient pointClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String jwtSecret;

    public UserService(UserRepository userRepository, PointClient pointClient,
        KafkaTemplate<String, String> kafkaTemplate, @Value("${jwt.secret}") String jwtSecret) {
        this.userRepository = userRepository;
        this.pointClient = pointClient;
        this.kafkaTemplate = kafkaTemplate;
        this.jwtSecret = jwtSecret;
    }

    @Transactional
    public void signup(SignUpRequestDto request) {
        User user = User.create(request.getEmail(), request.getName(), request.getPassword(), 0);
        User savedUser = userRepository.save(user);

        // 회원가입하면 포인트 1000점 적립
        pointClient.addPoints(savedUser.getUserId(), 1000);

        // '회원가입 완료' 이베트 발행
        UserSignedUpEvent userSignedUpEvent = new UserSignedUpEvent(
            savedUser.getUserId(),
            savedUser.getName()
        );
        this.kafkaTemplate.send(
            "user.signed-up",
            toJsonString(userSignedUpEvent)
        );
    }

    // 객체를 Json 형태의 String으로 만들어주는 메서드
    private String toJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = objectMapper.writeValueAsString(object);
            return message;
        } catch (Exception e) {
            throw new RuntimeException("JSON 직렬화 실패");
        }
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
//        throw new RuntimeException("에러 발생"); // 보상 트랜잭션 테스트 하기 위해서 예외 던짐
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT를 만들 때 사용하는 Key 생성 (공식 문서 방식)
        SecretKey secretKey = Keys.hmacShaKeyFor(
            jwtSecret.getBytes(StandardCharsets.UTF_8)
        );

        // JWT 토큰 만들기
        String token = Jwts.builder()
            .subject(user.getUserId().toString())
            .signWith(secretKey)
            .compact();

        return new LoginResponseDto(token);

    }
}
