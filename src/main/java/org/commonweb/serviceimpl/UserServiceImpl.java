package org.commonweb.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import org.commonweb.dto.request.UserCreationRequest;
import org.commonweb.dto.request.UserUpdateRequest;
import org.commonweb.entity.User;
import org.commonweb.repository.UserRepository;
import org.commonweb.security.HtmlSanitizer;
import org.commonweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateUser(String userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!userId.equals(updateRequest.getUserId()) && userRepository.existsByUserId(updateRequest.getUserId())) {
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

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public User createUserFromRequest(UserCreationRequest request) {
        if (userRepository.existsByUserId(request.getUserId())) {
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
        return userRepository.save(user);
    }

}
