package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CommentResponse {
    private UUID commentId;
    private UUID blogId;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String content;
    private UUID parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isVisible;
    private List<CommentResponse> replies; // Nested replies
    private int replyCount; // Số lượng reply
    private Boolean canEdit; // Có thể sửa comment này không (cho frontend)
    private Boolean canDelete; // Có thể xóa comment này không (cho frontend)
}