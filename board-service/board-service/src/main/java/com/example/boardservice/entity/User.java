package com.example.boardservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

  // Auto Increment 옵션을 사용하지 않을 거기 때문에
  // @GeneratedValue() 어노테이션 작성하지 않음
  @Id
  private Long userId;

  private String name;

}
