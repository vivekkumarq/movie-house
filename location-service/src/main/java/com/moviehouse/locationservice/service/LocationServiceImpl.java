package com.moviehouse.locationservice.service;

import com.moviehouse.locationservice.dataaccess.model.Location;
import com.moviehouse.locationservice.exception.LocationNotFound;
import com.moviehouse.locationservice.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    private static final String LOCATION_NOT_FOUND = "Location not found";

    @Autowired
    private LocationRepository locationRepository;

    @Override
    @Transactional
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() -> new LocationNotFound(LOCATION_NOT_FOUND));
    }

    @Override
    public Page<Location> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    @Override
    public List<Location> getLocationsByCity(String city) {
        return locationRepository.findByCityIgnoreCase(city);
    }

    @Override
    @Transactional
    public void deleteLocationById(UUID id) {
        if (!locationRepository.existsById(id)) {
            throw new LocationNotFound(LOCATION_NOT_FOUND);
        }
        locationRepository.deleteById(id);
    }
}
