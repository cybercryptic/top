package com.example.top.service;

import com.example.top.entity.order.ServiceType;
import com.example.top.repository.ServiceTypeRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class ServiceTypeService {

    @Autowired
    private ServiceTypeRepository repository;

    public void saveServiceType(ServiceType type) {
        if (type == null) {
            log.severe("Cannot add null as a service type");
            return;
        }

        repository.save(type);

        log.info("Service type with the name '" + type.getName() + "' is saved");
    }

    public List<ServiceType> findAllServiceTypes() {
        var serviceTypes = repository.findAll();

        log.info("Successfully retrieved all service types");
        return serviceTypes;
    }

    public ServiceType getServiceType(Long id) {
        var optServiceType = repository.findById(id);

        if (optServiceType.isEmpty()) {
            log.severe("No service type found with the id '" + id + "'");
            return null;
        }

        var serviceType = optServiceType.get();
        log.info("Service type with the name '" + serviceType.getName() + "' has been retrieved");
        return serviceType;
    }

    public void updateServiceType(ServiceType type) {
        repository.save(type);

        log.info("Service type with the name '" + type.getName() + "' has been updated");
    }

    public void deleteServiceType(Long id) {
        repository.deleteById(id);

        log.info("Service type with the id '" + id + "' has been deleted");
    }
}
