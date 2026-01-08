package com.example.pointservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "points")
@Getter
@NoArgsConstructor
public class Point {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pointId;

  private Long userId;

  private int amount;

  public Point(Long userId, int amount) {
    this.userId = userId;
    this.amount = amount;
  }

  // 포인트 적립 (나중에 Service 로직 짤 때 사용)
  public void addAmount(int amount) {
    this.amount += amount;
  }

  // 포인트 차감 (나중에 Service 로직 짤 때 사용)
  public void deductAmount(int amount) {
    this.amount -= amount;
  }

}
