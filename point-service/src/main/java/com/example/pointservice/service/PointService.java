package com.example.pointservice.service;

import com.example.pointservice.dto.request.AddPointRequestDto;
import com.example.pointservice.dto.request.DeductPointRequestDto;
import com.example.pointservice.entity.Point;
import com.example.pointservice.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

  private final PointRepository pointRepository;

  @Transactional
  public void addPoints(AddPointRequestDto addPointRequestDto) {
    Point point = pointRepository.findByUserId(addPointRequestDto.getUserId())
        .orElseGet(() -> new Point(addPointRequestDto.getUserId(), 0));

    point.addAmount(addPointRequestDto.getAmount());

    pointRepository.save(point);
  }

  @Transactional
  public void deductPoints(DeductPointRequestDto deductPointRequestDto) {
    Point point = pointRepository.findByUserId(deductPointRequestDto.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("사용자의 포인트 정보를 찾을 수 없습니다."));

    point.deductAmount(deductPointRequestDto.getAmount());

    pointRepository.save(point);
  }
}