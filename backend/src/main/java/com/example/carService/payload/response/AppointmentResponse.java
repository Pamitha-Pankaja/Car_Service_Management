package com.example.carService.payload.response;

import com.example.carService.models.Appointment;
import com.example.carService.models.Services;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AppointmentResponse  {
    Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int approved;
    private VehicleResponse vehicle;
    private ServicesResponse services;
    private UserResponse user;

}
