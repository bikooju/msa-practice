package com.example.pointservice.dto.request;

import lombok.Getter;

@Getter
public class AddPointRequestDto {
  private Long userId;
  private int amount;
}
