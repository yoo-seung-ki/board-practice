package org.commonweb.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.commonweb.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private Long id;
    private String content;
    private String userId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
