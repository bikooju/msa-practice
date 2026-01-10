package com.example.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignedUpEvent {
  private Long userId;
  private String name;
}
