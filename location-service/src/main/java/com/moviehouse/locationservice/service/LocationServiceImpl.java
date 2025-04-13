package com.moviehouse.locationservice.service;

import com.moviehouse.locationservice.dataaccess.model.Location;
import com.moviehouse.locationservice.exception.LocationNotFound;
import com.moviehouse.locationservice.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    private static final String LOCATION_NOT_FOUND = "Location not found";

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() ->new LocationNotFound(LOCATION_NOT_FOUND));
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public void deleteLocationById(UUID id) {
        locationRepository.deleteById(id);
    }
}
