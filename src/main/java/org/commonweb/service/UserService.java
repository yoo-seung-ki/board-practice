package org.commonweb.service;

import org.commonweb.dto.request.UserUpdateRequest;
import org.commonweb.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);
    User updateUser(String userId, UserUpdateRequest updateRequest);
    Optional<User> findByUserId(String userId);
    List<User> getAllUsers();
    public Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber); // 추가된 메서드
}
