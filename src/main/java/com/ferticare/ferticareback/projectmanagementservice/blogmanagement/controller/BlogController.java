package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.ManagerOnly;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.BlogRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response.BlogResponse;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.BlogService;
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
            @RequestHeader(value = "Authorization", required = false) String authHeader, 
            @PathVariable UUID id) {
        try {
            BlogResponse blog;
            if (authHeader != null) {
                blog = blogService.getBlogByIdWithAuth(authHeader, id);
            } else {
                blog = blogService.getById(id);
                // Only allow published blogs for unauthenticated users
                if (!"published".equalsIgnoreCase(blog.getStatus())) {
                    return ResponseEntity.status(401).body(null);
                }
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
            return ResponseEntity.ok("Deleted");
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
            return ResponseEntity.ok("Blog archived.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/approve")
    @ManagerOnly
    public ResponseEntity<?> approveBlog(@PathVariable UUID id) {
        try {
            blogService.approveBlog(id);
            return ResponseEntity.ok("Blog approved and published.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all")
    @ManagerOnly
    public ResponseEntity<List<BlogResponse>> getAllBlogsForManager() {
        return ResponseEntity.ok(blogService.getAllBlogs());
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
}
