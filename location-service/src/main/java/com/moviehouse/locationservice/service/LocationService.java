package com.moviehouse.locationservice.service;

import com.moviehouse.locationservice.dataaccess.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    Location addLocation(Location location);
    Location getLocationById(UUID id);
    Page<Location> getAllLocations(Pageable pageable);
    List<Location> getLocationsByCity(String city);
    void deleteLocationById(UUID id);
}
