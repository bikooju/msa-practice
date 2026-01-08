package com.example.boardservice.dto.response;

import lombok.Getter;

@Getter
public class UserResponseDto {
  private Long userId;
  private String email;
  private String name;
}
