package com.example.boardservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardCreatedEvent {
  private Long userId;
}
