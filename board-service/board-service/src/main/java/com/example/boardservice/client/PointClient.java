package com.example.boardservice.client;

import com.example.boardservice.dto.request.AddPointRequestDto;
import com.example.boardservice.dto.request.DeductPointRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PointClient {

  private final RestClient restClient;

  public PointClient(
      @Value("${client.point-service.url}") String pointServiceUrl
  ) {
    this.restClient = RestClient.builder()
        .baseUrl(pointServiceUrl)
        .build();
  }

  public void deductPoint(Long userId, int amount) {
    DeductPointRequestDto deductPointRequestDto = new DeductPointRequestDto(userId, amount);
    this.restClient.post()
        .uri("/internal/points/deduct")
        .contentType(MediaType.APPLICATION_JSON)
        .body(deductPointRequestDto)
        .retrieve()
        .toBodilessEntity();
  }

  public void addPoints(Long userId, int amount) {
    AddPointRequestDto addPointsRequestDto = new AddPointRequestDto(userId, amount);
    this.restClient.post()
        .uri("/internal/points/add")
        .contentType(MediaType.APPLICATION_JSON)
        .body(addPointsRequestDto)
        .retrieve()
        .toBodilessEntity();
  }

}
