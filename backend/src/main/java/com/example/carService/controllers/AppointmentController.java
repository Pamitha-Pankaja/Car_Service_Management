//package com.example.carService.controllers;
//
//import com.example.carService.models.Appointment;
//import com.example.carService.security.services.AppointmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/appointments")
//public class AppointmentController {
//
//    @Autowired
//    private AppointmentService appointmentService;
//
//    @PostMapping
//    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
//        try {
//            Appointment createdAppointment = appointmentService.createAppointment(appointment);
//            return ResponseEntity.ok(createdAppointment);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    // Additional endpoints can be added here as needed
//}



package com.example.carService.controllers;

import com.example.carService.models.Appointment;
import com.example.carService.security.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(@RequestParam LocalDate date, @RequestParam Long serviceId) {
        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(date, serviceId);
        return ResponseEntity.ok(availableSlots);
    }
//    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
//            @RequestParam Long serviceId) {
//
//        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(date, serviceId);
//        return ResponseEntity.ok(availableSlots);
//    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
        Appointment bookedAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(bookedAppointment);
    }
}

