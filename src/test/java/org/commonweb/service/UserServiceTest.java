package org.commonweb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.commonweb.dto.request.UserCreationRequest;
import org.commonweb.entity.User;
import org.commonweb.mapper.UserMapper;
import org.commonweb.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmailTest() {
        String email = "test@example.com";
        // User 객체 생성 시 @Builder를 사용
        User mockUser = User.builder()
                .email(email)
                .build();
        when(userMapper.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userService.findByEmail(email);
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
    }

    @Test
    void createUserFromRequestTest() {
        // UserCreationRequest 테스트 데이터 설정
        UserCreationRequest request = new UserCreationRequest();
        request.setUserId("testUser");
        request.setPassword("password");
        request.setEmail("test@example.com");
        request.setName("Test Name");
        request.setPhoneNumber("1234567890");

        User user = User.builder()
                .userId(request.getUserId())
                .password(request.getPassword())
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        doNothing().when(userMapper).createUser(any(User.class)); // userMapper의 createUser 호출을 모의

        // createUserFromRequest 메서드를 호출하고 결과 검증
        User createdUser = userService.createUserFromRequest(request);
        assertNotNull(createdUser);
        assertEquals(request.getUserId(), createdUser.getUserId());
        assertEquals(request.getEmail(), createdUser.getEmail());
        assertEquals(request.getName(), createdUser.getName());
        assertEquals(request.getPhoneNumber(), createdUser.getPhoneNumber());
        verify(userMapper).createUser(any(User.class)); // userMapper의 createUser 메서드가 호출되었는지 검증
    }

    @Test
    void findByPhoneNumberTest() {
        String phoneNumber = "1234567890";
        // User 객체 생성 시 @Builder를 사용
        User mockUser = User.builder()
                .phoneNumber(phoneNumber)
                .build();
        when(userMapper.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userService.findByPhoneNumber(phoneNumber);
        assertTrue(foundUser.isPresent());
        assertEquals(phoneNumber, foundUser.get().getPhoneNumber());
    }

    @Test
    void findByUserIdTest() {
        String userId = "testUserId";
        User mockUser = User.builder()
                .id(1L)
                .userId(userId)
                .password("testPassword")
                .email("testEmail")
                .name("testName")
                .phoneNumber("testPhoneNumber")
                .build();
        when(userMapper.findByUserId(userId)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUserOpt  = userService.findByUserId(userId);

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
        List<User> users = Arrays.asList(new User(), new User()); // 예시로 2개의 User 객체 생성
        when(userMapper.getAllUsers()).thenReturn(users); // userMapper의 getAllUsers 메서드가 users 리스트를 반환하도록 설정

        List<User> result = userService.getAllUsers(); // 실제 서비스 메서드 호출

        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(2, result.size()); // 반환된 리스트의 크기가 예상과 일치하는지 확인
    }

    // 기타 테스트 메서드...
}
