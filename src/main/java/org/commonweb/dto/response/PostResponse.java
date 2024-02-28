package org.commonweb.dto.response;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.commonweb.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
