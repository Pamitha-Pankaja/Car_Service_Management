package com.example.carService.payload.response;

import com.example.carService.models.ServiceCategory;

import java.util.List;


@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ServicesResponse {
    private long id;
    private String name;
    private String description;
    private Double cost;
    private ServiceCategory category;
    private Integer slots;
    private List<String> fixedTimeSlots;
    private Integer timePeriod;

}
