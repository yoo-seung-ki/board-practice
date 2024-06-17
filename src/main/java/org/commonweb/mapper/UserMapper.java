package org.commonweb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.commonweb.entity.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> getAllUsers();
    void createUser(User user);
    void updateUser(User user);
}
