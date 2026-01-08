package com.example.boardservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddPointRequestDto {
  private Long userId;
  private int amount;
}
