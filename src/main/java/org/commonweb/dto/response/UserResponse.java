package org.commonweb.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String userId;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
}
