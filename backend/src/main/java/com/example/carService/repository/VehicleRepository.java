package com.example.carService.repository;

import com.example.carService.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    // Add custom query methods if needed
    List<Vehicle> findByVehicleNo(String brand);
    List<Vehicle> findByOwnerId(Long userId);
}

