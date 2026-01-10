package com.example.userservice.controller;

import com.example.userservice.dto.request.LoginRequestDto;
import com.example.userservice.dto.request.SignUpRequestDto;
import com.example.userservice.dto.response.LoginResponseDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") //외부 클라이언트용 API 주소 : /api/____
public class UserController {

  private final UserService userService;

  @PostMapping("/sign-up")
  public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto request) {
    userService.signup(request);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(
      @RequestBody LoginRequestDto loginRequestDto
  ) {
    LoginResponseDto responseDto = userService.login(loginRequestDto);
    return ResponseEntity.ok(responseDto);
  }

}
