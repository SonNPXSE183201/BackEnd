package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.ManagerOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.BlogRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response.BlogResponse;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.BlogService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
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
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BlogRequest request) {
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);
            User user = userService.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String email = user.getEmail();
            request.setStatus("draft");
            return ResponseEntity.ok(blogService.create(request, email));
        }
        
        throw new RuntimeException("Invalid authorization token");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogById(@RequestHeader("Authorization") String authHeader, @PathVariable UUID id) {
        BlogResponse blog = blogService.getById(id);
        if (!"published".equalsIgnoreCase(blog.getStatus())) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String userId = jwtUtil.extractUserId(token);
                User user = userService.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                boolean isAuthor = blog.getAuthorName().equals(user.getFullName());
                Role role = roleRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Role not found"));
                boolean isManager = role.getRoleLevel() == 3;

                if (!isAuthor && !isManager) {
                    throw new RuntimeException("Access denied: only author or manager can view this blog");
                }
            } else {
                throw new RuntimeException("Authorization required to view unpublished blog");
            }
        }
        return ResponseEntity.ok(blog);
    }

    @GetMapping("/published")
    public ResponseEntity<List<BlogResponse>> getPublishedBlogs() {
        return ResponseEntity.ok(blogService.getPublishedBlogs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@RequestHeader("Authorization") String authHeader, @PathVariable UUID id) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);
            User user = userService.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String email = user.getEmail();
            blogService.deleteBlog(id, email);
            return ResponseEntity.ok("Deleted");
        }
        
        throw new RuntimeException("Invalid authorization token");
    }


    @PutMapping("/{id}/archive")
    public ResponseEntity<?> archiveBlog(@RequestHeader("Authorization") String authHeader, @PathVariable UUID id) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);
            User user = userService.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String email = user.getEmail();
            blogService.archiveBlog(id, email);
            return ResponseEntity.ok("Blog archived.");
        }
        
        throw new RuntimeException("Invalid authorization token");
    }

    @PutMapping("/{id}/approve")
    @ManagerOnly
    public ResponseEntity<?> approveBlog(@PathVariable UUID id) {
        blogService.approveBlog(id);
        return ResponseEntity.ok("Blog approved and published.");
    }

    @GetMapping("/all")
    @ManagerOnly
    public ResponseEntity<List<BlogResponse>> getAllBlogsForManager() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @GetMapping("/my")
    public ResponseEntity<List<BlogResponse>> getMyBlogs(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);
            User user = userService.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String email = user.getEmail();
            return ResponseEntity.ok(blogService.getBlogsByUser(email));
        }
        
        throw new RuntimeException("Invalid authorization token");
    }


}
