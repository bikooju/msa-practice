package com.example.boardservice.dto.response;

import com.example.boardservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {
  private Long boardId;
  private String title;
  private String content;
  private UserDto user;
}
