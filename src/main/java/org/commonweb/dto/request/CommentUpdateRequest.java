package org.commonweb.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {
    @NotBlank(message = "Content cannot be blank")
    private String content; // 수정할 내용
}

