package com.example.boardservice.dto;

import lombok.Getter;

@Getter
public class UserDto {
  private final Long userId;
  private final String name;

  public UserDto(Long userId, String name) {
    this.userId = userId;
    this.name = name;
  }
}
