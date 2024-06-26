package org.commonweb.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.commonweb.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
