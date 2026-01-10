package com.example.userservice.controller;

import com.example.userservice.dto.request.AddActivityScoreRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users") //내부 클라이언트용 API 주소 : /internal/____
public class UserInternalController {

  private final UserService userService;

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
    UserResponseDto userResponseDto = userService.getUser(userId);
    return ResponseEntity.ok(userResponseDto);
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getUsersByIds(
      @RequestParam List<Long> ids
  ) {
    List<UserResponseDto> userResponseDtos = userService.getUsersByIds(ids);
    return ResponseEntity.ok(userResponseDtos);
  }

  @PostMapping("/activity-score/add")
  public ResponseEntity<Void> addActivityScore(
      @RequestBody AddActivityScoreRequestDto addActivityScoreRequestDto
  ) {
    userService.addActivityScore(addActivityScoreRequestDto);
    return ResponseEntity.noContent().build();
  }
}
