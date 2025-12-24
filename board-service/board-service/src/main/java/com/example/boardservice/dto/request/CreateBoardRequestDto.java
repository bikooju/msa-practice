package com.example.boardservice.dto.request;

import lombok.Getter;

@Getter
public class CreateBoardRequestDto {
    private String title;

    private String content;

    private Long userId;
}

