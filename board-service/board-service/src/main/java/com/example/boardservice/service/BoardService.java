package com.example.boardservice.service;

import com.example.boardservice.client.PointClient;
import com.example.boardservice.client.UserClient;
import com.example.boardservice.dto.UserDto;
import com.example.boardservice.dto.request.CreateBoardRequestDto;
import com.example.boardservice.dto.response.BoardResponseDto;
import com.example.boardservice.dto.response.UserResponseDto;
import com.example.boardservice.entity.Board;
import com.example.boardservice.repository.BoardRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserClient userClient;
    private final PointClient pointClient;

    public void create(CreateBoardRequestDto createBoardRequestDto) {

        // 게시글 저장을 성공했는지 판단하는 플래그
        boolean isBoardCreated = false;
        Long savedBoardId = null;

        // 포인트 차감을 성공했는지 판단하는 플래그
        boolean isPointDeducted = false;

        try {
            // 게시글 작성 전 100 포인트 차감
            pointClient.deductPoint(createBoardRequestDto.getUserId(), 100);
            isPointDeducted = true; // 포인트 차감 성공 플래그
            System.out.println("포인트 차감 성공");

            Board board = new Board(
                createBoardRequestDto.getTitle(),
                createBoardRequestDto.getContent(),
                createBoardRequestDto.getUserId()
            );

            Board savedBoard = boardRepository.save(board);
            savedBoardId = savedBoard.getBoardId();
            isBoardCreated = true; // 게시글 저장 성공 플래그
            System.out.println("게시글 저장 성공");

            // 게시글 작성 시 작성자에게 활동 점수 10점 부여
            userClient.addActivityScore(createBoardRequestDto.getUserId(), 10);
            System.out.println("포인트 적립 성공");
        } catch (Exception e) {
            if (isBoardCreated) {
                // 게시글 작성 보상 트랜잭션 => 게시글 삭제
                this.boardRepository.deleteById(savedBoardId);
                System.out.println("[보상 트랜잭션] 게시글 삭제");
            }

            if (isPointDeducted) {
                // 포인트 차감 보상 트랜잭션 => 포인트 적립
                pointClient.addPoints(createBoardRequestDto.getUserId(), 100);
                System.out.println("[보상 트랜잭션] 포인트 적립");
            }

            // 실패 응답으로 처리하기 위해 예외 던지기
            throw e;
        }


    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // user-service로부터 사용자 정보 불러오기
        Optional<UserResponseDto> optionalUserResponseDto  = userClient.fetchUser(board.getUserId());

        // UserDto 생성
        UserDto userDto = null;
        if (optionalUserResponseDto.isPresent()) {
            UserResponseDto userResponseDto = optionalUserResponseDto.get();
            userDto = new UserDto(
                userResponseDto.getUserId(),
                userResponseDto.getName()
            );
        }

        // ResponseDto 생성
        BoardResponseDto boardResponseDto = new BoardResponseDto(
            board.getBoardId(),
            board.getTitle(),
            board.getContent(),
            userDto
        );

        return boardResponseDto;
    }

    // 게시글 전체 조회
    public List<BoardResponseDto> getBoards() {
        List<Board> boards = boardRepository.findAll();

        // userId 목록 추출
        List<Long> userIds = boards.stream()
            .map(Board::getUserId)
            .distinct() // 중복 제거
            .toList();

        // user-service로부터 사용자 정보 불러오기
        List<UserResponseDto> userResponseDtos = userClient.fetchUsersByIds(userIds);

        // userId를 Key로 하는 Map 생성
        Map<Long, UserDto> userMap = new HashMap<>();

        // null이 아닌 경우
        for (UserResponseDto userResponseDto : userResponseDtos) {
            Long userId = userResponseDto.getUserId();
            String name = userResponseDto.getName();
            userMap.put(userId, new UserDto(userId, name));
        }

        // 게시글 정보와 사용자 정보를 조합해서 BoardResponseDto 만들기
        return boards.stream()
            .map(board -> new BoardResponseDto(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                userMap.get(board.getUserId()) // 맵에서 UserDto 가져오기
            ))
            .toList();
    }
}
