package com.moviehouse.userservice.service;

import com.moviehouse.userservice.client.LocationClient;
import com.moviehouse.userservice.dataaccess.model.Location;
import com.moviehouse.userservice.dataaccess.model.LoginRequest;
import com.moviehouse.userservice.dataaccess.model.Reference;
import com.moviehouse.userservice.dataaccess.model.User;
import com.moviehouse.userservice.exception.DuplicateUsernameException;
import com.moviehouse.userservice.exception.LocationNotFoundException;
import com.moviehouse.userservice.exception.UnauthorizedUserException;
import com.moviehouse.userservice.exception.UserNotFoundException;
import com.moviehouse.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private LocationClient locationClient;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("vivek123");
        user.setPassword("plaintext");
        user.setName("Vivek");
        user.setPhoneNumber("9999999999");
        user.setLocation(new Reference(UUID.randomUUID(), "Bengaluru"));
    }

    @Test
    void addUserStoresTheEncodedPassword() {
        when(userRepository.existsByUsername("vivek123")).thenReturn(false);
        when(locationClient.getLocation(user.getLocation().getId())).thenReturn(new Location());
        when(passwordEncoder.encode("plaintext")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.addUser(user);

        assertThat(saved.getPassword()).isEqualTo("encoded");
    }

    @Test
    void addUserRejectsAnExistingUsername() {
        when(userRepository.existsByUsername("vivek123")).thenReturn(true);

        assertThatThrownBy(() -> userService.addUser(user))
                .isInstanceOf(DuplicateUsernameException.class);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void addUserRejectsAnUnknownLocation() {
        when(userRepository.existsByUsername("vivek123")).thenReturn(false);
        when(locationClient.getLocation(user.getLocation().getId())).thenReturn(null);

        assertThatThrownBy(() -> userService.addUser(user))
                .isInstanceOf(LocationNotFoundException.class);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserByIdReportsAMissingUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUserByIdReportsAMissingUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUserById(id))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    void loginReturnsTheUserWhenThePasswordMatches() {
        user.setPassword("encoded");
        LoginRequest login = new LoginRequest();
        login.setUsername("vivek123");
        login.setPassword("plaintext");
        when(userRepository.findByUsername("vivek123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plaintext", "encoded")).thenReturn(true);

        assertThat(userService.login(login)).isSameAs(user);
    }

    @Test
    void loginRejectsAWrongPassword() {
        user.setPassword("encoded");
        LoginRequest login = new LoginRequest();
        login.setUsername("vivek123");
        login.setPassword("wrong");
        when(userRepository.findByUsername("vivek123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThatThrownBy(() -> userService.login(login))
                .isInstanceOf(UnauthorizedUserException.class);
    }

    @Test
    void loginRejectsAnUnknownUsername() {
        LoginRequest login = new LoginRequest();
        login.setUsername("ghost");
        login.setPassword("whatever");
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(login))
                .isInstanceOf(UnauthorizedUserException.class);
    }
}
