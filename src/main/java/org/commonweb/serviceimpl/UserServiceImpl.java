package org.commonweb.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import org.commonweb.dto.request.UserCreationRequest;
import org.commonweb.dto.request.UserUpdateRequest;
import org.commonweb.entity.User;
import org.commonweb.repository.UserRepository;
import org.commonweb.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(String userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.updateUserInfo(
                updateRequest.getUserId(),
                updateRequest.getPassword(),
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
        User user = User.builder()
                .userId(request.getUserId())
                .password(request.getPassword())
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        return userRepository.save(user);
    }

}
