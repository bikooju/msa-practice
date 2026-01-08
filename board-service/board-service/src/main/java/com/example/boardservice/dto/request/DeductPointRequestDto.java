package com.example.boardservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeductPointRequestDto {
  private Long userAId;
  private int amount;
}
