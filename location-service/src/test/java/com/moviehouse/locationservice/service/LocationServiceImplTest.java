package com.moviehouse.locationservice.service;

import com.moviehouse.locationservice.dataaccess.model.Location;
import com.moviehouse.locationservice.exception.LocationNotFound;
import com.moviehouse.locationservice.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void getLocationByIdReturnsTheStoredLocation() {
        UUID id = UUID.randomUUID();
        Location location = new Location();
        location.setId(id);
        location.setCity("Bengaluru");
        when(locationRepository.findById(id)).thenReturn(Optional.of(location));

        assertThat(locationService.getLocationById(id)).isSameAs(location);
    }

    @Test
    void getLocationByIdReportsAMissingLocation() {
        UUID id = UUID.randomUUID();
        when(locationRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.getLocationById(id))
                .isInstanceOf(LocationNotFound.class)
                .hasMessage("Location not found");
    }

    @Test
    void deleteLocationByIdReportsAMissingLocation() {
        UUID id = UUID.randomUUID();
        when(locationRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> locationService.deleteLocationById(id))
                .isInstanceOf(LocationNotFound.class);
        verify(locationRepository, never()).deleteById(id);
    }

    @Test
    void deleteLocationByIdRemovesAnExistingLocation() {
        UUID id = UUID.randomUUID();
        when(locationRepository.existsById(id)).thenReturn(true);

        locationService.deleteLocationById(id);

        verify(locationRepository).deleteById(id);
    }
}
