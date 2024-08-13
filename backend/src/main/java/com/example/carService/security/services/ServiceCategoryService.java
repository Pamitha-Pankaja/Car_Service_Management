package com.example.carService.security.services;

import com.example.carService.models.ServiceCategory;
import com.example.carService.models.Services;
import com.example.carService.payload.response.ServiceCategoryResponse;
import com.example.carService.payload.response.ServicesResponse;
import com.example.carService.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

//    public List<ServiceCategory> getAllCategories() {
//        return serviceCategoryRepository.findAll();
//    }


    public List<ServiceCategoryResponse> getAllCategories() {
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        List<ServiceCategoryResponse> serviceCategoryResponseList = RefactorResponse(serviceCategoryList);
        return serviceCategoryResponseList;
    }


    public Optional<ServiceCategory> getCategoryById(Long id) {
        return serviceCategoryRepository.findById(id);
    }

    public ServiceCategory createCategory(ServiceCategory category) {
        return serviceCategoryRepository.save(category);
    }

    public ServiceCategory updateCategory(Long id, ServiceCategory categoryDetails) {
        ServiceCategory category = serviceCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryDetails.getName());
        return serviceCategoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        ServiceCategory category = serviceCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        serviceCategoryRepository.delete(category);
    }


    private List<ServiceCategoryResponse> RefactorResponse(List<ServiceCategory> servicesCategoryList) {
        List<ServiceCategoryResponse> serviceCategoryResponseList = new ArrayList<>();
        for (ServiceCategory serviceCategories : servicesCategoryList) {
            ServiceCategoryResponse servicesCategoryResponse = new ServiceCategoryResponse();
            servicesCategoryResponse.setId(serviceCategories.getId());
            servicesCategoryResponse.setName(serviceCategories.getName());


            serviceCategoryResponseList.add(servicesCategoryResponse);
        }
        return serviceCategoryResponseList;
    }
}
