package com.example.boardservice.event;

import lombok.Getter;
import tools.jackson.databind.ObjectMapper;

@Getter
public class UserSignedUpEvent {
  private Long userId;
  private String name;

  // 역직렬화(String 형태의 카프카 메시지 -> Java 객체)시 빈생성자 필요함
  public UserSignedUpEvent() {
  }

  // JSON 값을 UserSignedUpEvent로 역직렬화 하는 메서드
  public static UserSignedUpEvent fromJson(String json) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json, UserSignedUpEvent.class);
    } catch (Exception e) {
      throw new RuntimeException("JSON 파싱 실패");
    }
  }

}
