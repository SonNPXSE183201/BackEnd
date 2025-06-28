package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class BlogResponse {
    private UUID blogId;
    private String authorName;
    private UUID authorId;
    private String title;
    private String content;
    private String coverImage;
    private List<String> images;
    private String tags;
    private String status;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID approvedBy;
    private LocalDateTime approvedAt;
    private String approverName;
}
