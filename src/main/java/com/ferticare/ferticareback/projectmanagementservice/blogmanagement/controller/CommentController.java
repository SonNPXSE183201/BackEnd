package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.CommentRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response.CommentResponse;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.impl.CommentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    // 1. Xem tất cả comment của blog (KHÔNG cần đăng nhập)
    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByBlog(@PathVariable UUID blogId) {
        List<CommentResponse> comments = commentService.getVisibleCommentsByBlog(blogId, null, false);
        return ResponseEntity.ok(comments);
    }

    // 2. Lấy số lượng comment của blog (KHÔNG cần đăng nhập)
    @GetMapping("/blog/{blogId}/count")
    public ResponseEntity<Integer> getCommentCountByBlog(@PathVariable UUID blogId) {
        int count = commentService.getCommentCountByBlog(blogId);
        return ResponseEntity.ok(count);
    }

    // 3. Tạo bình luận mới (CẦN đăng nhập)
    @PostMapping
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request) {
        UUID userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            CommentResponse created = commentService.createComment(userId, request);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 4. Cập nhật comment (CẦN đăng nhập - chỉ người tạo mới được phép)
    @PutMapping("/{commentId}")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable UUID commentId, @Valid @RequestBody CommentRequest request) {
        UUID userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            CommentResponse updated = commentService.updateComment(commentId, userId, request.getContent());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 5. Ẩn comment (CẦN đăng nhập - chỉ người tạo hoặc admin mới được phép)
    @DeleteMapping("/{commentId}")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> hideComment(@PathVariable UUID commentId) {
        UUID userId = getCurrentUserId();
        boolean isAdmin = isCurrentUserAdmin();
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            commentService.hideComment(commentId, userId, isAdmin);
            return ResponseEntity.ok("Comment and all replies hidden successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6. Ẩn chỉ comment hiện tại (không ảnh hưởng đến comment con)
    @DeleteMapping("/{commentId}/only")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> hideCommentOnly(@PathVariable UUID commentId) {
        UUID userId = getCurrentUserId();
        boolean isAdmin = isCurrentUserAdmin();
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            commentService.hideCommentOnly(commentId, userId, isAdmin);
            return ResponseEntity.ok("Comment hidden successfully (replies remain visible).");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Helper methods để tự động lấy thông tin user
    private UUID getCurrentUserId() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    !authentication.getPrincipal().equals("anonymousUser")) {

                // Kiểm tra xem principal có phải là CustomerUserDetails không
                if (authentication.getPrincipal() instanceof com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.CustomerUserDetails) {
                    return ((com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.CustomerUserDetails) authentication.getPrincipal()).getUserId();
                } else {
                    // Fallback cho trường hợp principal là String
                    return UUID.fromString(authentication.getPrincipal().toString());
                }
            }
        } catch (Exception e) {
            // User chưa đăng nhập hoặc token không hợp lệ
            System.err.println("Error getting current user ID: " + e.getMessage());
        }
        return null;
    }

    private boolean isCurrentUserAdmin() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    !authentication.getPrincipal().equals("anonymousUser")) {
                return authentication.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ADMIN"));
            }
        } catch (Exception e) {
            // User chưa đăng nhập hoặc token không hợp lệ
            System.err.println("Error checking admin role: " + e.getMessage());
        }
        return false;
    }
}