package com.moviehouse.locationservice.controller;

import com.moviehouse.locationservice.dataaccess.model.Location;
import com.moviehouse.locationservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/location-info-management/location")
@CrossOrigin("*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> addLocation(@Valid @RequestBody Location location) {
        return new ResponseEntity<>(locationService.addLocation(location), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Location>> getAllLocations(
            @PageableDefault(size = 50, sort = "city", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(locationService.getAllLocations(pageable));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Location>> getLocationsByCity(@PathVariable String city) {
        return ResponseEntity.ok(locationService.getLocationsByCity(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationById(@PathVariable("id") UUID id) {
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
