package org.commonweb.repository;

import org.commonweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userid); // 사용자 아이디로 사용자 찾기
    Optional<User> findByEmail(String email); // 이메일로 사용자 찾기
    Optional<User> findByPhoneNumber(String phonenumber); // 전화번호로 사용자 찾기
    boolean existsByUserId(String userid); // 사용자 아이디가 존재하는지 확인
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
