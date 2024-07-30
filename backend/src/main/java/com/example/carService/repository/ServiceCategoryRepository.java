package com.example.carService.repository;

import com.example.carService.models.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    // Custom query methods can be defined here if needed
}

