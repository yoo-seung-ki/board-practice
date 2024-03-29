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
import org.commonweb.impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        // 필요한 경우, 테스트를 위한 Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지, 페이지 당 10개 항목

        // 모킹: UserRepository의 findAll 메서드가 Pageable을 사용하여 호출될 때의 동작 정의
        Page<User> mockPage = new PageImpl<>(Arrays.asList(new User(), new User()));
        when(userRepository.findAll(pageable)).thenReturn(mockPage);

        // 테스트 실행
        Page<User> result = userService.getAllUsers(pageable);

        // 검증: 결과가 예상대로인지 확인
        assertNotNull(result);
        assertEquals(2, result.getContent().size()); // 모킹된 결과와 일치하는지 검증
    }

    // 기타 테스트 메서드...
}
