package org.commonweb.controller;

import jakarta.persistence.EntityNotFoundException;
import org.commonweb.dto.request.UserCreationRequest;
import org.commonweb.dto.request.UserUpdateRequest;
import org.commonweb.dto.response.UserResponse;
import org.commonweb.entity.User;
import org.commonweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreationRequest creationRequest) {
        User savedUser = userService.createUserFromRequest(creationRequest);
        UserResponse userResponse = convertToUserResponse(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserByUserId(@PathVariable String userId) {
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with userId: " + userId));
        UserResponse userResponse = convertToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest updateRequest) {
        // UserUpdateRequest DTO를 사용하여 사용자 정보 업데이트 로직 수행
        User updatedUser = userService.updateUser(userId, updateRequest);

        // UserResponse DTO를 생성하여 응답
        UserResponse userResponse = convertToUserResponse(updatedUser);
        return ResponseEntity.ok(userResponse);
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        // 추가적으로, User 엔티티의 다른 필드들도 여기에 매핑할 수 있습니다.
        return userResponse;
    }
}
