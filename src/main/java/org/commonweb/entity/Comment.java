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
public class Comment {

    private Long id;

    private Post post;

    private User user;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now(); // 내용 변경 시 수정 시간 업데이트
    }

    public void setUpdatedAt(LocalDateTime time) {
        this.updatedAt = time;
    }
}
