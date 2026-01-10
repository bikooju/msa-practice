package com.example.boardservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveUserRequestDto {
  private Long userId;
  private String name;
}
