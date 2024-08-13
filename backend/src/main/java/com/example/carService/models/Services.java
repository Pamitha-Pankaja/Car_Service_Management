//package com.example.carService.models;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Table(name = "services")
//public class Services {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank
//    private String name;
//
//    private String description;
//
//    private Double cost;
//
//    @Column(name = "time_period")
//    private Integer timePeriod; // Time required for the service, in minutes
//
//    @Column(name = "slots")
//    private Integer slots; // Number of available slots for the service
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private ServiceCategory category;
//
//    @ManyToMany(mappedBy = "services")
//    private Set<Appointment> appointments = new HashSet<>();
//
//    // Getters and Setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Double getCost() {
//        return cost;
//    }
//
//    public void setCost(Double cost) {
//        this.cost = cost;
//    }
//
//    public Integer getTimePeriod() {
//        return timePeriod;
//    }
//
//    public void setTimePeriod(Integer timePeriod) {
//        this.timePeriod = timePeriod;
//    }
//
//    public Integer getSlots() {
//        return slots;
//    }
//
//    public void setSlots(Integer slots) {
//        this.slots = slots;
//    }
//
//    public ServiceCategory getCategory() {
//        return category;
//    }
//
//    public void setCategory(ServiceCategory category) {
//        this.category = category;
//    }
//
//    public Set<Appointment> getAppointments() {
//        return appointments;
//    }
//
//    public void setAppointments(Set<Appointment> appointments) {
//        this.appointments = appointments;
//    }
//}


package com.example.carService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private Double cost;

    @Column(name = "time_period")
    private Integer timePeriod; // Time required for the service, in minutes

    @Column(name = "slots")
    private Integer slots; // Number of available slots for the service

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "service_time_slots", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "fixed_time_slot")
    private List<String> fixedTimeSlots;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(Integer timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<String> getFixedTimeSlots() {
        return fixedTimeSlots;
    }

    public void setFixedTimeSlots(List<String> fixedTimeSlots) {
        this.fixedTimeSlots = fixedTimeSlots;
    }
}
