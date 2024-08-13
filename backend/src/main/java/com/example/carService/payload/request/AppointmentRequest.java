package com.example.carService.payload.request;

import com.example.carService.models.Services;
import com.example.carService.models.Technician;
import com.example.carService.models.User;
import com.example.carService.models.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AppointmentRequest {
        private Vehicle vehicle;
        private Technician technician;
        private User user;
        private LocalDate date;
        private LocalTime startTime;
        private Services service;
}
