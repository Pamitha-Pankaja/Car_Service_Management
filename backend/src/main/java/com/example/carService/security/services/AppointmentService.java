//
//package com.example.carService.security.services;
//
//import com.example.carService.models.Appointment;
//import com.example.carService.models.Services;
//import com.example.carService.models.Technician;
//import com.example.carService.models.Vehicle;
//import com.example.carService.repository.AppointmentRepository;
//import com.example.carService.repository.ServiceRepository;
//import com.example.carService.repository.TechnicianRepository;
//import com.example.carService.repository.VehicleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.*;
//import java.util.stream.Collectors;
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
//        // Initialize available slots with zero booked slots
//        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
//        LocalTime start = LocalTime.of(8, 0);
//        LocalTime end = LocalTime.of(18, 0);
//
//        while (start.plusMinutes(duration).isBefore(end) || start.plusMinutes(duration).equals(end)) {
//            bookedSlots.put(start, 0);
//            start = start.plusMinutes(duration);
//        }
//
//        // Update booked slots with current appointments
//        for (Appointment appointment : appointments) {
//            LocalTime appointmentStart = appointment.getStartTime();
//            LocalTime appointmentEnd = appointment.getEndTime();
//
//            for (LocalTime time : bookedSlots.keySet()) {
//                if (time.isBefore(appointmentEnd) && time.plusMinutes(duration).isAfter(appointmentStart)) {
//                    bookedSlots.put(time, bookedSlots.get(time) + 1);
//                }
//            }
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
//        LocalTime endTime = appointment.getEndTime();
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
//        // Check if the selected time slot overlaps with existing appointments
//        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
//        for (Appointment existingAppointment : appointments) {
//            LocalTime existingStartTime = existingAppointment.getStartTime();
//            LocalTime existingEndTime = existingAppointment.getEndTime();
//
//            // Check for overlap with existing appointments
//            if (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
//                for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(service.getTimePeriod())) {
//                    bookedSlots.put(time, bookedSlots.getOrDefault(time, 0) + 1);
//                }
//            }
//        }
//
//        // Check if the selected time slot has available slots
//        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(service.getTimePeriod())) {
//            if (bookedSlots.getOrDefault(time, 0) >= totalSlots) {
//                throw new RuntimeException("No available slots for the selected time.");
//            }
//        }
//
//        return appointmentRepository.save(appointment);
//    }
//}
//


package com.example.carService.security.services;

import com.example.carService.models.Appointment;
import com.example.carService.models.Services;
import com.example.carService.models.User;
import com.example.carService.models.Vehicle;
import com.example.carService.payload.request.AppointmentRequest;
import com.example.carService.payload.response.*;
import com.example.carService.repository.AppointmentRepository;
import com.example.carService.repository.ServiceRepository;
import com.example.carService.repository.UserRepository;
import com.example.carService.repository.VehicleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;


    public static final String ACCOUNT_SID = "AC017d20421440805fb53e3ab4b8bb88a3";
    public static final String AUTH_TOKEN = "bdc71685db25336c5e461c1baec73b6a";

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
//        // Initialize available slots with zero booked slots
//        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
//
//        // Parse the fixed time slots
//        List<String> fixedTimeSlots = service.getFixedTimeSlots();
//        for (String slot : fixedTimeSlots) {
//            String[] times = slot.split("-");
//            LocalTime start = LocalTime.parse(times[0]);
//            LocalTime end = LocalTime.parse(times[1]);
//
//            while (start.plusMinutes(duration).isBefore(end) || start.plusMinutes(duration).equals(end)) {
//                bookedSlots.put(start, 0);
//                start = start.plusMinutes(duration);
//            }
//        }
//
//        // Update booked slots with current appointments
//        for (Appointment appointment : appointments) {
//            LocalTime appointmentStart = appointment.getStartTime();
//            LocalTime appointmentEnd = appointment.getEndTime();
//
//            for (LocalTime time : bookedSlots.keySet()) {
//                if (time.isBefore(appointmentEnd) && time.plusMinutes(duration).isAfter(appointmentStart)) {
//                    bookedSlots.put(time, bookedSlots.get(time) + 1);
//                }
//            }
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

    public List<LocalTime> getAvailableTimeSlots(LocalDate date, Long serviceId) {
        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getTimePeriod();
        int totalSlots = service.getSlots();

        List<Appointment> appointments = appointmentRepository.findByDateAndServiceId(date, serviceId);

        // Filter out disapproved appointments
        appointments = appointments.stream()
                .filter(a -> a.isApproved() != 2)
                .collect(Collectors.toList());

        Map<LocalTime, Integer> bookedSlots = new HashMap<>();

        List<String> fixedTimeSlots = service.getFixedTimeSlots();
        for (String slot : fixedTimeSlots) {
            String[] times = slot.split("-");
            LocalTime start = LocalTime.parse(times[0]);
            LocalTime end = LocalTime.parse(times[1]);

            while (start.plusMinutes(duration).isBefore(end) || start.plusMinutes(duration).equals(end)) {
                bookedSlots.put(start, 0);
                start = start.plusMinutes(duration);
            }
        }

        for (Appointment appointment : appointments) {
            LocalTime appointmentStart = appointment.getStartTime();
            LocalTime appointmentEnd = appointmentStart.plusMinutes(duration);

            for (LocalTime time : bookedSlots.keySet()) {
                if (time.isBefore(appointmentEnd) && time.plusMinutes(duration).isAfter(appointmentStart)) {
                    bookedSlots.put(time, bookedSlots.get(time) + 1);
                }
            }
        }

        List<LocalTime> availableSlots = new ArrayList<>();
        for (Map.Entry<LocalTime, Integer> entry : bookedSlots.entrySet()) {
            if (entry.getValue() < totalSlots) {
                availableSlots.add(entry.getKey());
            }
        }

        return availableSlots;
    }




//    public Appointment createAppointment(AppointmentRequest appointment) {
//        LocalDate date = appointment.getDate();
//        LocalTime startTime = appointment.getStartTime();
////        LocalTime endTime = appointment.getEndTime();
//        Long serviceId = appointment.getService().getId();
//
//        // Fetch the service details
//        Services service = serviceRepository.findById(serviceId)
//                .orElseThrow(() -> new RuntimeException("Service not found"));
//        int totalSlots = service.getSlots();
//
//        // Retrieve all appointments for the specified date and service
//        List<Appointment> appointments = appointmentRepository.findByDateAndServiceId(date, serviceId);
//        List<Vehicle> vehicles = vehicleRepository.findByVehicleNo(appointment.getVehicle().getVehicleNo());
//        if (vehicles.isEmpty()){
//            vehicleRepository.save(appointment.getVehicle());
//        }else {
//            Vehicle  vehicle = appointment.getVehicle();
//            vehicle.setId(vehicles.get(0).getId());
//        }
//        // Check if the selected time slot overlaps with existing appointments
//        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
//        for (Appointment existingAppointment : appointments) {
//            LocalTime existingStartTime = existingAppointment.getStartTime();
//            LocalTime existingEndTime = existingStartTime.plusMinutes(service.getTimePeriod());
//
//            // Check for overlap with existing appointments
//            if (startTime.isBefore(existingEndTime) && startTime.plusMinutes(service.getTimePeriod()).isAfter(existingStartTime)) {
//                for (LocalTime time = startTime; time.isBefore(startTime.plusMinutes(service.getTimePeriod())); time = time.plusMinutes(service.getTimePeriod())) {
//                    bookedSlots.put(time, bookedSlots.getOrDefault(time, 0) + 1);
//                }
//            }
//        }
//
//        // Check if the selected time slot has available slots
//        for (LocalTime time = startTime; time.isBefore(startTime.plusMinutes(service.getTimePeriod())); time = time.plusMinutes(service.getTimePeriod())) {
//            if (bookedSlots.getOrDefault(time, 0) >= totalSlots) {
//                throw new RuntimeException("No available slots for the selected time.");
//            }
//        }
//        Appointment appointment1 = new Appointment();
//        appointment1.setVehicle(appointment.getVehicle());
//        appointment1.setTechnician(appointment.getTechnician());
//        appointment1.setService(appointment.getService());
//        appointment1.setUser(appointment.getUser());
//        appointment1.setStartTime(appointment.getStartTime());
//        appointment1.setDate(appointment.getDate());
//        appointment1.setEndTime(appointment.getStartTime().plusMinutes(service.getTimePeriod()));
//        return appointmentRepository.save(appointment1);
//    }


    public Appointment createAppointment(AppointmentRequest appointment) {
        LocalDate date = appointment.getDate();
        LocalTime startTime = appointment.getStartTime();
        Long serviceId = appointment.getService().getId();

        // Fetch the service details
        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        int totalSlots = service.getSlots();
        int duration = service.getTimePeriod();

        // Retrieve all appointments for the specified date and service, excluding disapproved ones
        List<Appointment> appointments = appointmentRepository.findByDateAndServiceId(date, serviceId).stream()
                .filter(a -> a.isApproved() != 2)
                .collect(Collectors.toList());

        // Ensure the vehicle is in the database
        List<Vehicle> vehicles = vehicleRepository.findByVehicleNo(appointment.getVehicle().getVehicleNo());
        if (vehicles.isEmpty()) {
            vehicleRepository.save(appointment.getVehicle());
        } else {
            Vehicle vehicle = appointment.getVehicle();
            vehicle.setId(vehicles.get(0).getId());
        }

        // Check if the selected time slot overlaps with existing appointments
        Map<LocalTime, Integer> bookedSlots = new HashMap<>();
        for (Appointment existingAppointment : appointments) {
            LocalTime existingStartTime = existingAppointment.getStartTime();
            LocalTime existingEndTime = existingStartTime.plusMinutes(duration);

            // Check for overlap with existing appointments
            for (LocalTime time = startTime; time.isBefore(startTime.plusMinutes(duration)); time = time.plusMinutes(duration)) {
                if (time.isBefore(existingEndTime) && time.plusMinutes(duration).isAfter(existingStartTime)) {
                    bookedSlots.put(time, bookedSlots.getOrDefault(time, 0) + 1);
                }
            }
        }

        // Check if the selected time slot has available slots
        for (LocalTime time = startTime; time.isBefore(startTime.plusMinutes(duration)); time = time.plusMinutes(duration)) {
            if (bookedSlots.getOrDefault(time, 0) >= totalSlots) {
                throw new RuntimeException("No available slots for the selected time.");
            }
        }

        // Create the new appointment
        Appointment newAppointment = new Appointment();
        newAppointment.setVehicle(appointment.getVehicle());
        newAppointment.setTechnician(appointment.getTechnician());
        newAppointment.setService(appointment.getService());
        newAppointment.setUser(appointment.getUser());
        newAppointment.setStartTime(appointment.getStartTime());
        newAppointment.setDate(appointment.getDate());
        newAppointment.setEndTime(startTime.plusMinutes(duration));

        return appointmentRepository.save(newAppointment);
    }




//    @Transactional
//    public ApproveAppointmentResponse approveAppointment(Long appointmentId ,int approveStatus) {
//        ApproveAppointmentResponse approveAppointmentResponse = new ApproveAppointmentResponse();
//        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
//        if (appointment.get().isApproved() == 0 && approveStatus == 1) {
//            appointment.get().setApproved(1);
//            Appointment updatedAppointment = appointmentRepository.save(appointment.get());
//            // Send SMS notification to the user
////            sendSmsNotification(updatedAppointment);
//
//            approveAppointmentResponse.setAppointmentId(updatedAppointment.getId());
//            if (updatedAppointment.isApproved() == 1) {
//                approveAppointmentResponse.setStatus("Appointment Placed");
//            }
//        } else if (approveStatus == 2) {
//            appointment.get().setApproved(2);
//            Appointment updatedAppointment = appointmentRepository.save(appointment.get());
//            // Send SMS notification to the user
////            sendSmsNotification(updatedAppointment);
//
//            approveAppointmentResponse.setAppointmentId(updatedAppointment.getId());
//            if (updatedAppointment.isApproved() == 2) {
//                approveAppointmentResponse.setStatus("Appointment Disallowed");
//            }
//        }
//        else{
//            approveAppointmentResponse.setAppointmentId(appointmentId);
//            approveAppointmentResponse.setStatus("Appointment Not Found");
//        }
//
//
//        return approveAppointmentResponse;
//    }


    @Transactional
    public ApproveAppointmentResponse approveAppointment(Long appointmentId, int approveStatus) {
        ApproveAppointmentResponse approveAppointmentResponse = new ApproveAppointmentResponse();
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();

            if (approveStatus == 1 && appointment.isApproved() == 0) {
                // Approve the appointment
                appointment.setApproved(1);
                appointmentRepository.save(appointment);
                approveAppointmentResponse.setAppointmentId(appointment.getId());
                approveAppointmentResponse.setStatus("Appointment Placed");

                // Send SMS notification to the user
              //sendSmsNotification(appointment);

            } else if (approveStatus == 2) {
                // Disapprove the appointment and make the slot available
                appointment.setApproved(2);
                appointmentRepository.save(appointment);
                // Here, since the appointment is disapproved, the slot is automatically available in your calculation logic
                approveAppointmentResponse.setAppointmentId(appointment.getId());
                approveAppointmentResponse.setStatus("Appointment Disallowed");

                // Send SMS notification to the user
                //sendSmsNotification(appointment);

            } else {
                approveAppointmentResponse.setAppointmentId(appointmentId);
                approveAppointmentResponse.setStatus("Invalid operation");
            }
        } else {
            approveAppointmentResponse.setAppointmentId(appointmentId);
            approveAppointmentResponse.setStatus("Appointment Not Found");
        }

        return approveAppointmentResponse;
    }




    private void sendSmsNotification(Appointment appointment) {
        // Replace with actual user phone number
        String userPhoneNumber = appointment.getUser().getPhoneNumber();
        String message = String.format("Your appointment on %s at %s has been approved.",
                appointment.getDate(), appointment.getStartTime());
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message messageText = Message.creator(
                        new com.twilio.type.PhoneNumber("+94703159274"),
                        new com.twilio.type.PhoneNumber("+19784403443"),
                        "Appointment Placed")
                .create();
        // Use Twilio or any other SMS service to send the message
        // Example with Twilio:
        // Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        // Message.creator(new PhoneNumber(userPhoneNumber), new PhoneNumber(TWILIO_PHONE_NUMBER), message).create();

        // For demonstration purposes, just print the message
        System.out.println("Sending SMS to " + userPhoneNumber + ": " + message);
    }



    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointmentList = appointmentRepository.findAll();
        List<AppointmentResponse> appointmentResponseList = RefactorResponse(appointmentList);

        return appointmentResponseList;
    }

    public List<AppointmentResponse> getServiceHistoryByUserId(Long userId) {
        List<Appointment> appointmentList = appointmentRepository.findByUserId(userId);
        List<AppointmentResponse> appointmentResponseList = RefactorResponse(appointmentList);
        return appointmentResponseList;
    }


    private List<AppointmentResponse> RefactorResponse(List<Appointment> appointmentList) {
        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            AppointmentResponse appointmentResponse = new AppointmentResponse();
            appointmentResponse.setId(appointment.getId());
            appointmentResponse.setDate(appointment.getDate());
            appointmentResponse.setStartTime(appointment.getStartTime());
            appointmentResponse.setEndTime(appointment.getEndTime());
            appointmentResponse.setApproved(appointment.isApproved());

            VehicleResponse VehicleResponse = new VehicleResponse();
            VehicleResponse.setType(appointment.getVehicle().getType());
            VehicleResponse.setId(appointment.getVehicle().getId());
            VehicleResponse.setVehicleNumber(appointment.getVehicle().getVehicleNo());
            VehicleResponse.setModel(appointment.getVehicle().getModel());
            appointmentResponse.setVehicle(VehicleResponse);


            // set Service
//            Services service = serviceRepository.findById(appointment.getService().getId()).orElseThrow(() -> new RuntimeException("Service not found"));
            ServicesResponse servicesResponse = new ServicesResponse();
            servicesResponse.setId(appointment.getService().getId());
            servicesResponse.setName(appointment.getService().getName());
            servicesResponse.setDescription(appointment.getService().getDescription());
            servicesResponse.setCost(appointment.getService().getCost());
            appointmentResponse.setServices(servicesResponse);


            //set user
            UserResponse user = new UserResponse();
            user.setId(appointment.getUser().getId());
            user.setName(appointment.getUser().getUsername());
            user.setPhoneNumber(appointment.getUser().getPhoneNumber());
            appointmentResponse.setUser(user);

//            appointmentResponse.setApproved(appointment.isApproved());
            appointmentResponseList.add(appointmentResponse);
        }
        return appointmentResponseList;
    }


}

