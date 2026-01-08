package com.example.pointservice.controller;

import com.example.pointservice.dto.request.AddPointRequestDto;
import com.example.pointservice.dto.request.DeductPointRequestDto;
import com.example.pointservice.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {

  private final PointService pointService;

  @PostMapping("/add")
  public ResponseEntity<Void> addPoints(
      @RequestBody AddPointRequestDto addPointRequestDto
  ) {
    pointService.addPoints(addPointRequestDto);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/deduct") // deduct : 빼다
  public ResponseEntity<Void> deductPoints(
      @RequestBody DeductPointRequestDto deductPointRequestDto
  ) {
    pointService.deductPoints(deductPointRequestDto);
    return ResponseEntity.noContent().build();
  }

}
