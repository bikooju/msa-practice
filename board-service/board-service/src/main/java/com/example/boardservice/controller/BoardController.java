package com.example.boardservice.controller;

import com.example.boardservice.dto.request.CreateBoardRequestDto;
import com.example.boardservice.dto.response.BoardResponseDto;
import com.example.boardservice.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody CreateBoardRequestDto request
    ) {
        boardService.create(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId) {
        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        return ResponseEntity.ok(boardResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<BoardResponseDto>> getBoards() {
        List<BoardResponseDto> boardResponseDtos = boardService.getBoards();
        return ResponseEntity.ok(boardResponseDtos);
    }
}
