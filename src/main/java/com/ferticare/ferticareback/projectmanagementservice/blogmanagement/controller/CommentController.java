package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.CommentRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.entity.Comment;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    // 1. Tạo bình luận mới
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Comment created = commentService.createComment(userId, request);
        return ResponseEntity.ok(created);
    }

    // 2. Lấy tất cả comment của blog
    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<Comment>> getCommentsByBlog(@PathVariable UUID blogId) {
        List<Comment> comments = commentService.getVisibleCommentsByBlog(blogId);
        return ResponseEntity.ok(comments);
    }

    // 3. Ẩn comment (chỉ người tạo hoặc admin mới được phép)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> hideComment(@PathVariable UUID commentId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getPrincipal().toString());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ADMIN"));

        commentService.hideComment(commentId, userId, isAdmin);
        return ResponseEntity.ok("Comment hidden successfully.");
    }
}