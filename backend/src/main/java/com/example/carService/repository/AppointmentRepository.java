package com.example.carService.repository;

import com.example.carService.models.Appointment;
import com.example.carService.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
//    List<Appointment> findByDateAndStartTimeBetween(LocalDate date, LocalTime startTime, LocalTime endTime);

    List<Appointment> findByDateAndServiceId(LocalDate date, Long serviceId);
}


