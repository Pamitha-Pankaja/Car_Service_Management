package com.example.carService.controllers;

import com.example.carService.models.Services;
import com.example.carService.payload.response.ServicesResponse;
import com.example.carService.security.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

//    @GetMapping
//    public List<Services> getAllServices() {
//        return serviceService.getAllServices();
//    }


    @GetMapping
    public ResponseEntity<List<ServicesResponse>> getAllServices() {
        List<ServicesResponse> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Services> getServiceById(@PathVariable Long id) {
        Optional<Services> service = serviceService.getServiceById(id);
        if (service.isPresent()) {
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<List<ServicesResponse>> getServiceByCategoryId(@PathVariable Long id) {
        List<ServicesResponse> service = serviceService.getServiceByCategoryId(id);
        if (!service.isEmpty()) {
            return ResponseEntity.ok(service);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Services createService(@RequestBody Services service) {
        return serviceService.createService(service);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> updateService(@PathVariable Long id, @RequestBody Services serviceDetails) {
        Services updatedService = serviceService.updateService(id, serviceDetails);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}

