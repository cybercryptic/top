package com.example.top.service;

import com.example.top.entity.order.Dimensions;
import com.example.top.repository.DimensionsRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class DimensionsService {

    @Autowired
    private DimensionsRepository repository;

    public void saveDimensions(Dimensions dimensions) {
        if (dimensions == null) {
            log.severe("Cannot add null as dimensions");
            return;
        }

        repository.save(dimensions);

        log.info("New dimensions '" + dimensions.getName() + "' is added");
    }

    public List<Dimensions> findAllDimensions() {
        var dimensions = repository.findAll();

        log.info("Successfully retrieved all dimensions");
        return dimensions;
    }

    public Dimensions getDimensions(Long id) {
        var dimensions = repository.getReferenceById(id);

        log.info("Dimensions with the name '" + dimensions.getName() + "' has been retrieved");
        return dimensions;
    }

    public void updateDimensions(Dimensions dimensions) {
        repository.save(dimensions);

        log.info("Dimensions with the name '" + dimensions.getName() + "' has been updated");
    }

    public void deleteDimensions(Long id) {
        repository.deleteById(id);

        log.info("Dimensions with the id '" + id + "' has been deleted");
    }
}