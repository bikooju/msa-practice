package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddActivityScoreRequestDto {
  private Long userId;
  private int score;
}
