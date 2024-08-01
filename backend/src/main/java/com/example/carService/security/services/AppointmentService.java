//package com.example.carService.security.services;
//
//import com.example.carService.models.Appointment;
//import com.example.carService.models.Services;
//import com.example.carService.repository.AppointmentRepository;
//import com.example.carService.repository.ServiceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class AppointmentService {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private ServiceRepository serviceRepository;
//
//    public Appointment createAppointment(Appointment appointment) {
//        // Calculate end time based on the services selected
//        LocalTime startTime = appointment.getStartTime();
//        int totalDuration = appointment.getServices().stream().mapToInt(Services::getTimePeriod).sum();
//        LocalTime endTime = startTime.plusMinutes(totalDuration);
//
//        // Check for conflicts
//        List<Appointment> existingAppointments = appointmentRepository.findByDateAndStartTimeBetween(
//                appointment.getDate(), startTime, endTime);
//
//        if (existingAppointments.size() >= getTotalSlots(appointment.getServices())) {
//            throw new IllegalArgumentException("No available slots for the selected time and services.");
//        }
//
//        appointment.setEndTime(endTime);
//        return appointmentRepository.save(appointment);
//    }
//
//    private int getTotalSlots(Set<Services> services) {
//        return services.stream().mapToInt(Services::getSlots).sum();
//    }
//}



package com.example.carService.security.services;

import com.example.carService.models.Appointment;
import com.example.carService.models.Services;
import com.example.carService.models.Technician;
import com.example.carService.models.Vehicle;
import com.example.carService.repository.AppointmentRepository;
import com.example.carService.repository.ServiceRepository;
import com.example.carService.repository.TechnicianRepository;
import com.example.carService.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


//@Service
//public class AppointmentService {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private ServiceRepository serviceRepository;
//
//    public List<LocalTime> getAvailableTimeSlots(LocalDate date, Long serviceId) {
//        Services service = serviceRepository.findById(serviceId)
//                .orElseThrow(() -> new RuntimeException("Service not found"));
//
//        int duration = service.getTimePeriod();
//        int totalSlots = service.getSlots();
//
//        // Retrieve all appointments for the specified date and service
//        List<Appointment> appointments = appointmentRepository.findByDateAndServiceId(date, serviceId);
//
//        // Track the number of booked slots for each time slot
//        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
//
//        // Initialize all available time slots with zero booked slots
//        LocalTime start = LocalTime.of(8, 0);
//        LocalTime end = LocalTime.of(18, 0);
//        while (start.plusMinutes(duration).isBefore(end) || start.plusMinutes(duration).equals(end)) {
//            bookedSlots.put(start, 0);
//            start = start.plusMinutes(duration);
//        }
//
//        // Count booked slots for each time slot
//        for (Appointment appointment : appointments) {
//            LocalTime startTime = appointment.getStartTime();
//            bookedSlots.put(startTime, bookedSlots.getOrDefault(startTime, 0) + 1);
//        }
//
//        // Filter available slots based on the number of available slots
//        List<LocalTime> availableSlots = new ArrayList<>();
//        for (Map.Entry<LocalTime, Integer> entry : bookedSlots.entrySet()) {
//            LocalTime time = entry.getKey();
//            int bookedCount = entry.getValue();
//            if (bookedCount < totalSlots) {
//                availableSlots.add(time);
//            }
//        }
//
//        return availableSlots;
//    }
//
//    public Appointment createAppointment(Appointment appointment) {
//        LocalDate date = appointment.getDate();
//        LocalTime startTime = appointment.getStartTime();
//        Long serviceId = appointment.getService().getId();
//
//        // Fetch the service details
//        Services service = serviceRepository.findById(serviceId)
//                .orElseThrow(() -> new RuntimeException("Service not found"));
//        int totalSlots = service.getSlots();
//
//        // Retrieve all appointments for the specified date and service
//        List<Appointment> appointments = appointmentRepository.findByDateAndServiceId(date, serviceId);
//
//        // Track the number of booked slots for each time slot
//        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
//        for (Appointment existingAppointment : appointments) {
//            LocalTime existingStartTime = existingAppointment.getStartTime();
//            bookedSlots.put(existingStartTime, bookedSlots.getOrDefault(existingStartTime, 0) + 1);
//        }
//
//        // Check if the selected time slot has available slots
//        if (bookedSlots.getOrDefault(startTime, 0) >= totalSlots) {
//            throw new RuntimeException("No available slots for the selected time.");
//        }
//
//        return appointmentRepository.save(appointment);
//    }
//}

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<LocalTime> getAvailableTimeSlots(LocalDate date, Long serviceId) {
        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getTimePeriod();
        int totalSlots = service.getSlots();

        // Retrieve all appointments for the specified date and service
        List<Appointment> appointments = appointmentRepository.findByDateAndServiceId(date, serviceId);

        // Initialize available slots with zero booked slots
        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(18, 0);

        while (start.plusMinutes(duration).isBefore(end) || start.plusMinutes(duration).equals(end)) {
            bookedSlots.put(start, 0);
            start = start.plusMinutes(duration);
        }

        // Update booked slots with current appointments
        for (Appointment appointment : appointments) {
            LocalTime appointmentStart = appointment.getStartTime();
            LocalTime appointmentEnd = appointment.getEndTime();

            for (LocalTime time : bookedSlots.keySet()) {
                if (time.isBefore(appointmentEnd) && time.plusMinutes(duration).isAfter(appointmentStart)) {
                    bookedSlots.put(time, bookedSlots.get(time) + 1);
                }
            }
        }

        // Filter available slots based on the number of available slots
        List<LocalTime> availableSlots = new ArrayList<>();
        for (Map.Entry<LocalTime, Integer> entry : bookedSlots.entrySet()) {
            LocalTime time = entry.getKey();
            int bookedCount = entry.getValue();
            if (bookedCount < totalSlots) {
                availableSlots.add(time);
            }
        }

        return availableSlots;
    }

    public Appointment createAppointment(Appointment appointment) {
        LocalDate date = appointment.getDate();
        LocalTime startTime = appointment.getStartTime();
        LocalTime endTime = appointment.getEndTime();
        Long serviceId = appointment.getService().getId();

        // Fetch the service details
        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        int totalSlots = service.getSlots();

        // Retrieve all appointments for the specified date and service
        List<Appointment> appointments = appointmentRepository.findByDateAndServiceId(date, serviceId);

        // Check if the selected time slot overlaps with existing appointments
        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
        for (Appointment existingAppointment : appointments) {
            LocalTime existingStartTime = existingAppointment.getStartTime();
            LocalTime existingEndTime = existingAppointment.getEndTime();

            // Check for overlap with existing appointments
            if (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
                for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(service.getTimePeriod())) {
                    bookedSlots.put(time, bookedSlots.getOrDefault(time, 0) + 1);
                }
            }
        }

        // Check if the selected time slot has available slots
        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(service.getTimePeriod())) {
            if (bookedSlots.getOrDefault(time, 0) >= totalSlots) {
                throw new RuntimeException("No available slots for the selected time.");
            }
        }

        return appointmentRepository.save(appointment);
    }
}

