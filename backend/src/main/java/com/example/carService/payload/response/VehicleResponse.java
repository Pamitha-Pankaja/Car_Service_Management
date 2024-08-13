package com.example.carService.payload.response;
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class VehicleResponse {
    private long id;
    private String model;
    private String type;
    private String vehicleNumber;
}
