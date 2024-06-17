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
public class Post {

    private Long id;

    private String title;

    private String content;

    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void updateInfo(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now(); // 수정 시간 업데이트
    }
}
