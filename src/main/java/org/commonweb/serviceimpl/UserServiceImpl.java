package org.commonweb.serviceimpl;

import org.commonweb.dto.request.UserCreationRequest;
import org.commonweb.dto.request.UserUpdateRequest;
import org.commonweb.entity.User;
import org.commonweb.exception.ResourceNotFoundException;
import org.commonweb.mapper.CommentMapper;
import org.commonweb.mapper.PostMapper;
import org.commonweb.mapper.UserMapper;

import org.commonweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserMapper userMapper, PostMapper postMapper, CommentMapper commentMapper,  PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateUser(String userId, UserUpdateRequest updateRequest) {
        User user = userMapper.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!userId.equals(updateRequest.getUserId()) && userMapper.findByUserId(updateRequest.getUserId()).isPresent()) {
            throw new IllegalStateException("New user ID already exists: " + updateRequest.getUserId());
        }

        String encodedPassword = updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()
                ? passwordEncoder.encode(updateRequest.getPassword())
                : user.getPassword();

        user.updateUserInfo(
                updateRequest.getUserId(),
                encodedPassword,
                updateRequest.getEmail(),
                updateRequest.getName(),
                updateRequest.getPhoneNumber());

        userMapper.updateUser(user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userMapper.findByPhoneNumber(phoneNumber);
    }

    public User createUserFromRequest(UserCreationRequest request) {
        if (userMapper.findByUserId(request.getUserId()).isPresent()) {
            throw new IllegalStateException("User already exists with id: " + request.getUserId());
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .userId(request.getUserId())
                .password(encodedPassword)
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        userMapper.createUser(user);
        return user;
    }

}
