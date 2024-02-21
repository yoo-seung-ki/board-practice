package org.commonweb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.commonweb.entity.User;
import org.commonweb.repository.UserRepository;
import org.commonweb.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmailTest() {
        String email = "test@example.com";
        // User 객체 생성 시 @Builder를 사용
        User mockUser = User.builder()
                .email(email)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userService.findByEmail(email);
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
    }

    @Test
    void saveUserTest() {
        User user = new User(); // 테스트 데이터 설정
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);
        assertEquals(user, savedUser);
        verify(userRepository).save(user);
    }

    @Test
    void findByPhoneNumberTest() {
        String phoneNumber = "1234567890";
        // User 객체 생성 시 @Builder를 사용
        User mockUser = User.builder()
                .phoneNumber(phoneNumber)
                .build();
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userRepository.findByPhoneNumber(phoneNumber);
        assertTrue(foundUser.isPresent());
        assertEquals(phoneNumber, foundUser.get().getPhoneNumber());
    }

    @Test
    void findUserByIdTest() {
        Long userId = 1L;
        User mockUser = User.builder()
                .id(userId)
                .userId("testUserid")
                .password("testPassword")
                .email("testEmail")
                .name("testName")
                .phoneNumber("testPhoneNumber")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUserOpt  = userService.findUserById(userId);

        assertTrue(foundUserOpt.isPresent(), "User should be found");
        foundUserOpt.ifPresent(foundUser -> {
            assertEquals(mockUser.getId(), foundUser.getId());
            assertEquals(mockUser.getUserId(), foundUser.getUserId());
            assertEquals(mockUser.getEmail(), foundUser.getEmail());
            assertEquals(mockUser.getName(), foundUser.getName());
            assertEquals(mockUser.getPhoneNumber(), foundUser.getPhoneNumber());
        });
    }

    @Test
    void getAllUsersTest() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAllUsers();
        assertEquals(mockUsers.size(), users.size());
        verify(userRepository).findAll();
    }

    // 기타 테스트 메서드...
}
