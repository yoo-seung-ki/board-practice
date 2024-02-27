package org.commonweb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationRequest {
    private String userId;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    // 추가적으로 유효성 검증을 위한 어노테이션을 사용할 수 있습니다.
}