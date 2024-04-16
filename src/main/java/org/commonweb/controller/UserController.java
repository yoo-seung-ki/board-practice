package org.commonweb.controller;

import org.commonweb.dto.request.UserCreationRequest;
import org.commonweb.dto.request.UserUpdateRequest;
import org.commonweb.dto.response.UserResponse;
import org.commonweb.entity.User;
import org.commonweb.security.HtmlSanitizer;
import org.commonweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class UserController {
    private final UserService userService;

    private final HtmlSanitizer htmlSanitizer;

    @Autowired
    public UserController(UserService userService, HtmlSanitizer htmlSanitizer) {
        this.userService = userService;
        this.htmlSanitizer = htmlSanitizer;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreationRequest creationRequest) {
        sanitizeUserCreationRequest(creationRequest);
        User savedUser = userService.createUserFromRequest(creationRequest);
        UserResponse userResponse = convertToUserResponse(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest updateRequest) {
        sanitizeUserUpdateRequest(updateRequest);
        User updatedUser = userService.updateUser(userId, updateRequest);
        UserResponse userResponse = convertToUserResponse(updatedUser);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserByUserId(@PathVariable String userId) {
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with userId: " + userId));
        UserResponse userResponse = convertToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
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

    private void sanitizeUserCreationRequest(UserCreationRequest request) {
        request.setUserId(htmlSanitizer.sanitize(request.getUserId()));
        request.setEmail(htmlSanitizer.sanitize(request.getEmail()));
        request.setName(htmlSanitizer.sanitize(request.getName()));
        request.setPhoneNumber(htmlSanitizer.sanitize(request.getPhoneNumber()));
    }

    private void sanitizeUserUpdateRequest(UserUpdateRequest request) {
        request.setUserId(htmlSanitizer.sanitize(request.getUserId()));
        request.setEmail(htmlSanitizer.sanitize(request.getEmail()));
        request.setName(htmlSanitizer.sanitize(request.getName()));
        request.setPhoneNumber(htmlSanitizer.sanitize(request.getPhoneNumber()));
    }
}
