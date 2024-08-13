package com.example.carService.payload.response;

import com.example.carService.models.ServiceCategory;


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

}
