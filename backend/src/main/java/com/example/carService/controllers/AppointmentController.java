//package com.example.carService.controllers;
//
//import com.example.carService.models.Appointment;
//import com.example.carService.security.services.AppointmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/appointments")
//public class AppointmentController {
//
//    @Autowired
//    private AppointmentService appointmentService;
//
//    @GetMapping("/available-slots")
//    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(@RequestParam LocalDate date, @RequestParam Long serviceId) {
//        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(date, serviceId);
//        return ResponseEntity.ok(availableSlots);
//    }
////    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(
////            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
////            @RequestParam Long serviceId) {
////
////        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(date, serviceId);
////        return ResponseEntity.ok(availableSlots);
////    }
//
//    @PostMapping("/book")
//    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
//        Appointment bookedAppointment = appointmentService.createAppointment(appointment);
//        return ResponseEntity.ok(bookedAppointment);
//    }
//}


package com.example.carService.controllers;

import com.example.carService.models.Appointment;
import com.example.carService.payload.request.AppointmentRequest;
import com.example.carService.payload.response.AppointmentResponse;
import com.example.carService.payload.response.ApproveAppointmentResponse;
import com.example.carService.security.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam Long serviceId) {
        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(date, serviceId);
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody AppointmentRequest appointment) {
        Appointment bookedAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(bookedAppointment);
    }

    @PutMapping("/approve/{appointmentId}/{approveStatus}")
    public ResponseEntity<ApproveAppointmentResponse> approveAppointment(@PathVariable Long appointmentId , @PathVariable int approveStatus) {
//        Appointment approvedAppointment = appointmentService.approveAppointment(appointmentId);
        ApproveAppointmentResponse approvedAppointment = appointmentService.approveAppointment(appointmentId,approveStatus);
        return ResponseEntity.ok(approvedAppointment);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        System.out.println(appointments);
        return ResponseEntity.ok(appointments);
    }


    @GetMapping("/history/{userId}")


    public ResponseEntity<List<AppointmentResponse>> getServiceHistoryByUserId(@PathVariable Long userId) {
        List<AppointmentResponse> serviceHistory = appointmentService.getServiceHistoryByUserId(userId);
        System.out.println(serviceHistory);
        return ResponseEntity.ok(serviceHistory);
    }




}


