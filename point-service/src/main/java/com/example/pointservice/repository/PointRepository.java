package com.example.pointservice.repository;

import com.example.pointservice.entity.Point;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
  Optional<Point> findByUserId(Long userId);
}
