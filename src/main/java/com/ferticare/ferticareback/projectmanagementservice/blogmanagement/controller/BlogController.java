package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.ManagerOnly;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.BlogRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response.BlogResponse;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.BlogService;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BlogRequest request) {
        try {
            BlogResponse response = blogService.createBlogWithAuth(authHeader, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogById(
            @PathVariable UUID id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            BlogResponse blog;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // Có authentication - sử dụng method có đếm view
                blog = blogService.getBlogByIdWithAuth(authHeader, id);
            } else {
                // Không có authentication - chỉ lấy blog và đếm view cơ bản
                blog = blogService.getById(id);

                // Chỉ cho phép xem blog đã xuất bản đối với người dùng chưa xác thực
                if (!"published".equalsIgnoreCase(blog.getStatus())) {
                    return ResponseEntity.status(401).body(null);
                }

                // Tăng view count cho blog đã xuất bản (không có tracking)
                blogService.incrementViewCount(id);
            }

            return ResponseEntity.ok(blog);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(null);
        }
    }

    @GetMapping("/published")
    public ResponseEntity<List<BlogResponse>> getPublishedBlogs() {
        return ResponseEntity.ok(blogService.getPublishedBlogs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {
        try {
            blogService.deleteBlogWithAuth(authHeader, id);
            return ResponseEntity.ok("Đã xóa");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<?> archiveBlog(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {
        try {
            blogService.archiveBlogWithAuth(authHeader, id);
            return ResponseEntity.ok("Đã lưu trữ blog.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/approve")
    @ManagerOnly
    public ResponseEntity<?> approveBlog(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);
            blogService.approveBlog(id, UUID.fromString(userId));
            return ResponseEntity.ok("Blog đã được duyệt và xuất bản.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all")
    @ManagerOnly
    public ResponseEntity<List<BlogResponse>> getAllBlogsForManager() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @GetMapping("/pending")
    @ManagerOnly
    public ResponseEntity<List<BlogResponse>> getPendingBlogs() {
        return ResponseEntity.ok(blogService.getPendingBlogs());
    }

    @GetMapping("/status/{status}")
    @ManagerOnly
    public ResponseEntity<List<BlogResponse>> getBlogsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(blogService.getBlogsByStatus(status));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BlogResponse>> getMyBlogs(
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<BlogResponse> blogs = blogService.getMyBlogsWithAuth(authHeader);
            return ResponseEntity.ok(blogs);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/{id}/view-count")
    public ResponseEntity<Integer> getBlogViewCount(@PathVariable UUID id) {
        try {
            BlogResponse blog = blogService.getById(id);
            return ResponseEntity.ok(blog.getViewCount());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
