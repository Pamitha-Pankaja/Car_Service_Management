package com.example.carService.security.services;

import com.example.carService.models.Services;
import com.example.carService.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<Services> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public Services createService(Services services) {
        return serviceRepository.save(services);
    }

    public Services updateService(Long id, Services servicesDetails) {
        Services services = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        services.setName(servicesDetails.getName());
        services.setDescription(servicesDetails.getDescription());
        services.setCost(servicesDetails.getCost());
        return serviceRepository.save(services);
    }

    public void deleteService(Long id) {
        Services services = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        serviceRepository.delete(services);
    }
}
