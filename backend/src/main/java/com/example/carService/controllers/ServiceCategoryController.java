package com.example.carService.controllers;

import com.example.carService.models.ServiceCategory;
import com.example.carService.payload.response.ServiceCategoryResponse;
import com.example.carService.payload.response.ServicesResponse;
import com.example.carService.security.services.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/service-categories")
public class ServiceCategoryController {

    @Autowired
    private ServiceCategoryService serviceCategoryService;


    @GetMapping
    public ResponseEntity<List<ServiceCategoryResponse>> getAllServices() {
        List<ServiceCategoryResponse> serviceCategories = serviceCategoryService.getAllCategories();
        return ResponseEntity.ok(serviceCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategory> getCategoryById(@PathVariable Long id) {
        Optional<ServiceCategory> category = serviceCategoryService.getCategoryById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ServiceCategory createCategory(@RequestBody ServiceCategory category) {
        return serviceCategoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategory> updateCategory(@PathVariable Long id, @RequestBody ServiceCategory categoryDetails) {
        ServiceCategory updatedCategory = serviceCategoryService.updateCategory(id, categoryDetails);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        serviceCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
