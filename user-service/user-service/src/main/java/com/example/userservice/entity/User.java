package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String name;

    private String password;

    private int activityScore = 0;

    public static User create(String email, String name, String password, int activityScore) {
        return new User(null, email, name, password, activityScore);
    }

    // 활동 점수 적립 (나중에 Service 로직 짤 때 사용)
    public void addActivityScore(int score) {
        this.activityScore += score;
    }


}
