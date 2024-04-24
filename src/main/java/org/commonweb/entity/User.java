package org.commonweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 회원가입시 자동으로 생성되며 관리자가 사용자를 식별할때 사용되고 숫자로만 이루어진 고유번호.

    @Column(nullable = false, unique = true, length = 50)
    private String userId; // 사용자가 회원가입시 직접 작성하는 고유한 문자열, 또는 문자열+숫자의 조합이며 사용자의 아이디 이다.

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String phoneNumber;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime userCreatedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime userUpdatedAt;

    public void updateUserInfo(String userId, String password, String name, String email, String phoneNumber) {
        if (userId != null) this.userId = userId;
        if (password != null) this.password = password;
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
    }
}
