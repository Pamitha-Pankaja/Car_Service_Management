package com.example.carService.repository;

import com.example.carService.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {
    // Custom query methods can be defined here if needed
}