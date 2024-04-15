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
    private final HtmlSanitizer htmlSanitizer;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, HtmlSanitizer htmlSanitizer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.htmlSanitizer = htmlSanitizer;
    }

    public User updateUser(String userId, UserUpdateRequest updateRequest) {
        /*
        String sanitizedOldUserId = htmlSanitizer.sanitize(userId);
        String sanitizedNewUserId = htmlSanitizer.sanitize(updateRequest.getUserId());

        User user = userRepository.findByUserId(sanitizedOldUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + sanitizedOldUserId));

        if (!sanitizedOldUserId.equals(sanitizedNewUserId) && userRepository.existsByUserId(sanitizedNewUserId)) {
            throw new IllegalStateException("New user ID already exists: " + sanitizedNewUserId);
        }

        String sanitizedOldPhoneNumber = htmlSanitizer.sanitize(user.getPhoneNumber());
        String sanitizedNewPhoneNumber = htmlSanitizer.sanitize(updateRequest.getPhoneNumber());

        if (!sanitizedOldPhoneNumber.equals(sanitizedNewPhoneNumber) && userRepository.existsByPhoneNumber(sanitizedNewPhoneNumber)) {
            throw new IllegalStateException("New phone number already exists: " + sanitizedNewPhoneNumber);
        }

        String sanitizedOldEmail = htmlSanitizer.sanitize(user.getEmail());
        String sanitizedNewEmail = htmlSanitizer.sanitize(updateRequest.getEmail());

        if (!sanitizedOldEmail.equals(sanitizedNewEmail) && userRepository.existsByEmail(sanitizedNewEmail)) {
            throw new IllegalStateException("New email already exists: " + sanitizedNewEmail);
        }

        String sanitizedName = htmlSanitizer.sanitize(updateRequest.getName());

        String encodedPassword = updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()
                ? passwordEncoder.encode(updateRequest.getPassword())
                : user.getPassword();

        user.updateUserInfo(
                sanitizedNewUserId,
                encodedPassword,
                sanitizedNewEmail,
                sanitizedName,
                sanitizedNewPhoneNumber);

        return userRepository.save(user);

         */

        String sanitizedOldUserId = htmlSanitizer.sanitize(userId);
        String sanitizedNewUserId = htmlSanitizer.sanitize(updateRequest.getUserId());

        User user = userRepository.findByUserId(sanitizedOldUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + sanitizedOldUserId));

        if (!sanitizedOldUserId.equals(sanitizedNewUserId) && userRepository.existsByUserId(sanitizedNewUserId)) {
            throw new IllegalStateException("New user ID already exists: " + sanitizedNewUserId);
        }

        // 이름 변경 확인
        String sanitizedName = updateRequest.getName() != null ? htmlSanitizer.sanitize(updateRequest.getName()) : user.getName();

        // 전화번호 변경 확인
        String sanitizedPhoneNumber = updateRequest.getPhoneNumber() != null ? htmlSanitizer.sanitize(updateRequest.getPhoneNumber()) : user.getPhoneNumber();

        // 이메일 변경 확인
        String sanitizedEmail = updateRequest.getEmail() != null ? htmlSanitizer.sanitize(updateRequest.getEmail()) : user.getEmail();

        // 비밀번호 변경 확인
        String encodedPassword = updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()
                ? passwordEncoder.encode(updateRequest.getPassword())
                : user.getPassword();

        user.updateUserInfo(
                sanitizedNewUserId,
                encodedPassword,
                sanitizedEmail,
                sanitizedName,
                sanitizedPhoneNumber);

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
        String sanitizedId = htmlSanitizer.sanitize(request.getUserId());
        String sanitizedEmail = htmlSanitizer.sanitize(request.getEmail());
        String sanitizedName = htmlSanitizer.sanitize(request.getName());
        String sanitizedPhoneNumber = htmlSanitizer.sanitize(request.getPhoneNumber());

        if (userRepository.existsByUserId(sanitizedId)) {
            throw new IllegalStateException("User already exists with id: " + sanitizedId);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .userId(sanitizedId)
                .password(encodedPassword)
                .email(sanitizedEmail)
                .name(sanitizedName)
                .phoneNumber(sanitizedPhoneNumber)
                .build();
        return userRepository.save(user);
    }

}
