package com.example.boardservice.service;

import com.example.boardservice.dto.request.CreateBoardRequestDto;
import com.example.boardservice.entity.Board;
import com.example.boardservice.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void create(CreateBoardRequestDto request) {
        Board board = Board.create(request.getTitle(), request.getContent(), request.getUserId());
        boardRepository.save(board);
    }
}
