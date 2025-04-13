package com.moviehouse.locationservice.controller;

import com.moviehouse.locationservice.dataaccess.model.Location;
import com.moviehouse.locationservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/location-info-management/location")
@CrossOrigin("*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    ResponseEntity<Location> addLocation(@RequestBody Location location){
        return new ResponseEntity<>(locationService.addLocation(location),HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<Location>> getAllLocations(){
        return new ResponseEntity<>(locationService.getAllLocations(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Location> getLocationById(@PathVariable("id") UUID id){
        return new ResponseEntity<>(locationService.getLocationById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteLocationById(@PathVariable("id") UUID id){
        locationService.deleteLocationById(id);
    }
}
