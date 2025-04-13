package com.moviehouse.locationservice.service;

import com.moviehouse.locationservice.dataaccess.model.Location;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    Location addLocation(Location location);
    Location getLocationById(UUID id);
    List<Location> getAllLocations();
    void deleteLocationById(UUID id);
}
