package com.example.pointservice.dto.request;

import lombok.Getter;

@Getter
public class DeductPointRequestDto {
  private Long userId;
  private int amount;
}
