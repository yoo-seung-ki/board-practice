package org.commonweb.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String userId;
    private String password;
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String phoneNumber;
}
