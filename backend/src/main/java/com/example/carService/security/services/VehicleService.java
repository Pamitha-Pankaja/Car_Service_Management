package com.example.carService.security.services;
import com.example.carService.models.ServiceCategory;
import com.example.carService.models.Services;
import com.example.carService.models.Vehicle;
import com.example.carService.payload.response.ServicesResponse;
import com.example.carService.payload.response.VehicleResponse;
import com.example.carService.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setMake(vehicleDetails.getMake());
                    vehicle.setModel(vehicleDetails.getModel());
                    vehicle.setVehicleNo(vehicleDetails.getVehicleNo());
                    vehicle.setType(vehicleDetails.getType());
                    vehicle.setOwner(vehicleDetails.getOwner());
                    return vehicleRepository.save(vehicle);
                })
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id " + id));
    }

    public List<VehicleResponse> getVehiclesByUserId(Long userId) {
        List<Vehicle> vehicletList = vehicleRepository.findByOwnerId(userId);
        List<VehicleResponse> vehicleResponseList = RefactorResponse(vehicletList);
        return vehicleResponseList;

    }

    private List<VehicleResponse> RefactorResponse(List<Vehicle> vehicleList) {
        List<VehicleResponse> vehicleResponseList = new ArrayList<>();
        for (Vehicle vehicles : vehicleList) {
            VehicleResponse VehicleResponse = new VehicleResponse();
            VehicleResponse.setType(vehicles.getType());
            VehicleResponse.setId(vehicles.getId());
            VehicleResponse.setVehicleNumber(vehicles.getVehicleNo());
            VehicleResponse.setModel(vehicles.getModel());
            vehicleResponseList.add(VehicleResponse);
        }
        return vehicleResponseList;
    }



}

