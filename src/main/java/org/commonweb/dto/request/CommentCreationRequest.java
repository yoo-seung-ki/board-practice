package org.commonweb.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreationRequest {

    private Long id;
    private Long postId;
    private String userId;
    @NotBlank(message = "Content cannot be blank")
    private String content;

}
