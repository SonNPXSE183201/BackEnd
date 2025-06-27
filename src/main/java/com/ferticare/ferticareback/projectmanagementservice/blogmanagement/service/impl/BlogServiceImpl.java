package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.BlogRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response.BlogResponse;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.entity.Blog;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.repository.BlogRepository;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.BlogService;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    @Override
    public BlogResponse create(BlogRequest request, String authorEmail) {
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = Blog.builder()
                .author(author)
                .title(request.getTitle())
                .content(request.getContent())
                .coverImage(request.getCoverImage())
                .tags(request.getTags())
                .status(request.getStatus())
                .build();

        Blog saved = blogRepository.save(blog);
        return toResponse(saved);
    }

    @Override
    public BlogResponse getById(UUID id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        return toResponse(blog);
    }

    @Override
    public List<BlogResponse> getPublishedBlogs() {
        return blogRepository.findByStatus("published").stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void archiveBlog(UUID id, String email) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        if (!blog.getAuthor().getEmail().equals(email)) {
            Role role = roleRepository.findByUser(blog.getAuthor())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            if (role.getRoleLevel() != 3) {
                throw new RuntimeException("Unauthorized to archive blog");
            }
        }

        blog.setStatus("archived");
        blog.setUpdatedAt(LocalDateTime.now());
        blogRepository.save(blog);
    }

    private BlogResponse toResponse(Blog blog) {
        return BlogResponse.builder()
                .blogId(blog.getBlogId())
                .authorName(blog.getAuthor().getFullName())
                .title(blog.getTitle())
                .content(blog.getContent())
                .coverImage(blog.getCoverImage())
                .tags(blog.getTags())
                .status(blog.getStatus())
                .viewCount(blog.getViewCount())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }

    @Override
    public void approveBlog(UUID blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setStatus("published");
        blog.setUpdatedAt(LocalDateTime.now());
        blogRepository.save(blog);
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<BlogResponse> getBlogsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return blogRepository.findByAuthor(user).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deleteBlog(UUID blogId, String email) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        boolean isAuthor = blog.getAuthor().getId().equals(user.getId());
        boolean isManager = role.getRoleLevel() == 3;

        if (!isAuthor && !isManager) {
            throw new RuntimeException("Access denied: only author or manager can delete");
        }

        blogRepository.delete(blog);
    }

    @Override
    public BlogResponse createBlogWithAuth(String authToken, BlogRequest request) {
        User user = getUserFromToken(authToken);
        request.setStatus("draft");
        return create(request, user.getEmail());
    }

    @Override
    public BlogResponse getBlogByIdWithAuth(String authToken, UUID id) {
        BlogResponse blog = getById(id);
        
        if (!"published".equalsIgnoreCase(blog.getStatus())) {
            User user = getUserFromToken(authToken);
            boolean isAuthor = blog.getAuthorName().equals(user.getFullName());
            Role role = roleRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            boolean isManager = role.getRoleLevel() == 3;

            if (!isAuthor && !isManager) {
                throw new RuntimeException("Access denied: only author or manager can view this blog");
            }
        }
        
        return blog;
    }

    @Override
    public void deleteBlogWithAuth(String authToken, UUID id) {
        User user = getUserFromToken(authToken);
        deleteBlog(id, user.getEmail());
    }

    @Override
    public void archiveBlogWithAuth(String authToken, UUID id) {
        User user = getUserFromToken(authToken);
        archiveBlog(id, user.getEmail());
    }

    @Override
    public List<BlogResponse> getMyBlogsWithAuth(String authToken) {
        User user = getUserFromToken(authToken);
        return getBlogsByUser(user.getEmail());
    }

    private User getUserFromToken(String authToken) {
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid authorization token");
        }
        
        String token = authToken.substring(7);
        String userId = jwtUtil.extractUserId(token);
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
