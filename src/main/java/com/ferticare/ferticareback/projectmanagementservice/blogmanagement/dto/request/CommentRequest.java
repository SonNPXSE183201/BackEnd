package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CommentRequest {
    @NotNull(message = "Blog ID không được để trống")
    private UUID blogId;      // ID của bài blog cần bình luận

    @NotBlank(message = "Nội dung comment không được để trống")
    @Size(min = 1, max = 1000, message = "Nội dung comment phải từ 1-1000 ký tự")
    private String content;   // Nội dung bình luận

    private UUID parentId;    // Nếu là reply thì đây là comment cha
}