package com.example.carService.security.services;

import com.example.carService.models.ServiceCategory;
import com.example.carService.models.Services;
import com.example.carService.payload.response.ServicesResponse;
import com.example.carService.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ServicesResponse> getAllServices() {
        List<Services> appointmentList = serviceRepository.findAll();
        List<ServicesResponse> appointmentResponseList = RefactorResponse(appointmentList);
        return appointmentResponseList;
    }

    public Optional<Services> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }
    public List<ServicesResponse> getServiceByCategoryId(Long id) {
        List<Services> appointmentList = serviceRepository.findServicesByCategoryId(id);
        List<ServicesResponse> appointmentResponseList = RefactorResponse(appointmentList);
        return appointmentResponseList;
    }

    public Services createService(Services services) {
        return serviceRepository.save(services);
    }

//    public Services updateService(Long id, Services servicesDetails) {
//        Services services = serviceRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Service not found"));
//
//        services.setName(servicesDetails.getName());
//        services.setDescription(servicesDetails.getDescription());
//        services.setCost(servicesDetails.getCost());
//        return serviceRepository.save(services);
//    }

    public Services updateService(Long id, Services servicesDetails) {
        Services services = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Update the fields
        services.setName(servicesDetails.getName());
        services.setDescription(servicesDetails.getDescription());
        services.setCost(servicesDetails.getCost());
        services.setSlots(servicesDetails.getSlots());
        services.setTimePeriod(servicesDetails.getTimePeriod());
        services.setFixedTimeSlots(servicesDetails.getFixedTimeSlots());

        // Save the updated entity
        return serviceRepository.save(services);
    }

    // Utility to convert a single `Services` object to `ServicesResponse`
    public ServicesResponse refactorSingleResponse(Services services) {
        ServicesResponse servicesResponse = new ServicesResponse();
        servicesResponse.setId(services.getId());
        servicesResponse.setName(services.getName());
        servicesResponse.setDescription(services.getDescription());
        servicesResponse.setCost(services.getCost());
        servicesResponse.setFixedTimeSlots(services.getFixedTimeSlots());
        servicesResponse.setSlots(services.getSlots());
        servicesResponse.setTimePeriod(services.getTimePeriod());

        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setId(services.getCategory().getId());
        serviceCategory.setName(services.getCategory().getName());
        servicesResponse.setCategory(serviceCategory);

        return servicesResponse;
    }


    public void deleteService(Long id) {
        Services services = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        serviceRepository.delete(services);
    }



    private List<ServicesResponse> RefactorResponse(List<Services> servicesList) {
        List<ServicesResponse> serviceResponseList = new ArrayList<>();
        for (Services services : servicesList) {
            ServicesResponse servicesResponse = new ServicesResponse();
            servicesResponse.setId(services.getId());
            servicesResponse.setName(services.getName());
            servicesResponse.setDescription(services.getDescription());
            servicesResponse.setTimePeriod(services.getTimePeriod());
            servicesResponse.setFixedTimeSlots(services.getFixedTimeSlots());
            servicesResponse.setSlots(services.getSlots());
            servicesResponse.setCost(services.getCost());
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setId(services.getCategory().getId());
            serviceCategory.setName(services.getCategory().getName());
            servicesResponse.setCategory(serviceCategory);
            serviceResponseList.add(servicesResponse);
        }
        return serviceResponseList;
    }
}
