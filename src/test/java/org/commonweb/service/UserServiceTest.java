package org.commonweb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.commonweb.dto.request.UserCreationRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.commonweb.entity.User;
import org.commonweb.repository.UserRepository;
import org.commonweb.serviceimpl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
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
    void createUserFromRequestTest() {
        // UserCreationRequest 테스트 데이터 설정
        UserCreationRequest request = new UserCreationRequest();
        request.setUserId("testUser");
        request.setPassword("password");
        request.setEmail("test@example.com");
        request.setName("Test Name");
        request.setPhoneNumber("1234567890");

        User user = new User(); // 예상되는 User 엔티티 결과
        when(userRepository.save(any(User.class))).thenReturn(user); // userRepository의 save 호출 시 user 반환 설정

        // createUserFromRequest 메서드를 호출하고 결과 검증
        User createdUser = userService.createUserFromRequest(request);
        assertEquals(user, createdUser); // 반환된 User 객체가 예상과 일치하는지 검증
        verify(userRepository).save(any(User.class)); // userRepository의 save 메서드가 호출되었는지 검증
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
    void findByUserIdTest() {
        String userId = "testUserId";
        User mockUser = User.builder()
                .id(1L)
                .userId("testUserid")
                .password("testPassword")
                .email("testEmail")
                .name("testName")
                .phoneNumber("testPhoneNumber")
                .build();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(mockUser));

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
        when(userRepository.findAll()).thenReturn(users); // userRepository의 findAll 메서드가 users 리스트를 반환하도록 설정

        List<User> result = userService.getAllUsers(); // 실제 서비스 메서드 호출

        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(2, result.size()); // 반환된 리스트의 크기가 예상과 일치하는지 확인
    }

    // 기타 테스트 메서드...
}
