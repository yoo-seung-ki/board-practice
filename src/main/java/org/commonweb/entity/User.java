package org.commonweb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id; // 사용자 회원가입시 자동으로 생성되며 관리자가 사용자를 식별할때 사용되고 숫자로만 이루어진 고유번호.

    private String userId; // 사용자가 회원가입시 직접 작성하는 고유한 문자열, 또는 문자열+숫자의 조합이며 사용자의 아이디 이다.

    private String password;

    private String email;

    private String name;

    private String phoneNumber;

    private LocalDateTime userCreatedAt;

    private LocalDateTime userUpdatedAt;

    public void updateUserInfo(String userId, String password, String name, String email, String phoneNumber) {
        if (userId != null) this.userId = userId;
        if (password != null) this.password = password;
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        this.userUpdatedAt = LocalDateTime.now(); // 수정 시간 업데이트
    }
}
