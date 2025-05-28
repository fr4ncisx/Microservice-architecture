package org.springcloud.users.domain.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springcloud.users.domain.models.dto.request.UserRequest;
import org.springcloud.users.domain.models.dto.response.UserResponse;
import org.springcloud.users.domain.models.entity.User;
import org.springcloud.users.infrastructure.persistence.repository.UserRepository;
import org.springcloud.users.utils.MappingUtils;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserResponse mockResponseUser;
    private UserRequest mockRequestUser;
    private User mockUser;
    private UUID mockId;

    /*@Disabled
    @BeforeEach
    void setUp() {
        mockUser = new User(UUID.randomUUID(), "john.foo@example.com", "12345", "john", "foo");
        mockResponseUser = new UserResponse(mockUser.getId(), "john.foo@example.com", "john", "foo");
        mockRequestUser = new UserRequest("john.foo@example.com", "12345", "john", "foo");
        mockId = mockUser.getId();
    }*/

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        var response = userService.findAll();
        assertEquals(List.of(mockResponseUser), response);
        verify(userRepository).findAll();

        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> userService.findAll());

        verify(userRepository, times(2)).findAll();
    }

    @Test
    void findById() {
        when(userRepository.findById(mockId)).thenReturn(Optional.of(mockUser));
        var response = userService.findById(mockId);
        assertEquals(mockResponseUser, response);

        when(userRepository.findById(mockId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.findById(mockId));

        verify(userRepository, times(2)).findById(mockId);
    }

    @Test
    void create() {
        var convertedToUser = MappingUtils.mapToClass(mockRequestUser, User.class);
        when(userRepository.save(convertedToUser))
                .thenReturn(mockUser);
        var response = userService.create(mockRequestUser);

        assertEquals(ResponseEntity.ok(mockResponseUser), ResponseEntity.ok(response));
        assertThrows(IllegalArgumentException.class, () -> userService.create(null));

        verify(userRepository, times(1)).save(convertedToUser);
    }

    @Test
    void update() {
        var randomUUID = UUID.randomUUID();
        when(userRepository.findById(mockId)).thenReturn(Optional.of(mockUser));
        var response = userService.update(mockId, mockRequestUser);

        assertEquals(Map.of("stats", "Los cambios se han actualizado exitosamente")
                , response);
        assertThrows(IllegalArgumentException.class, () -> userService.update(randomUUID, mockRequestUser));

        verify(userRepository, times(1)).findById(mockId);
        verify(userRepository, times(1)).findById(randomUUID);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void delete() {
        when(userRepository.findById(mockId)).thenReturn(Optional.of(mockUser));
        doNothing().when(userRepository).delete(mockUser);

        var response = userService.delete(mockId);
        assertEquals(Map.of("stats", "El usuario ha sido eliminado"), response);

        when(userRepository.findById(mockId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.delete(mockId));

        verify(userRepository, times(2)).findById(mockId);
        verify(userRepository, times(1)).delete(mockUser);
    }
}