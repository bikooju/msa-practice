package com.example.userservice.client;

import com.example.userservice.dto.request.AddPointRequestDto;
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

  public void addPoints(Long userId, int amount) {
    AddPointRequestDto addPointsRequestDto = new AddPointRequestDto(userId, amount);
    this.restClient.post()
        .uri("/points/add")
        .contentType(MediaType.APPLICATION_JSON)
        .body(addPointsRequestDto)
        .retrieve()
        .toBodilessEntity();
  }
}