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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    // Cache để tránh đếm lượt xem nhiều lần từ cùng một người dùng trong một phiên
    private final ConcurrentHashMap<String, Long> viewTracking = new ConcurrentHashMap<>();

    private static final long VIEW_EXPIRY_TIME = 30 * 60 * 1000; // 30 phút

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
                .status(request.getStatus() != null ? request.getStatus() : "pending") // Mặc định là "pending"
                .build();

        // Thêm danh sách hình ảnh nếu có
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            blog.setImages(new ArrayList<>(request.getImages()));
        }

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
    public List<BlogResponse> getPendingBlogs() {
        return blogRepository.findByStatus("pending").stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BlogResponse> getBlogsByStatus(String status) {
        return blogRepository.findByStatus(status).stream()
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
                throw new RuntimeException("Không có quyền lưu trữ blog");
            }
        }

        blog.setStatus("archived");
        blog.setUpdatedAt(LocalDateTime.now());
        blogRepository.save(blog);
    }

    @Override
    public void incrementViewCount(UUID blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setViewCount(blog.getViewCount() + 1);
        blogRepository.save(blog);
    }

    private BlogResponse toResponse(Blog blog) {
        User approver = null;
        if (blog.getApprovedBy() != null) {
            approver = userRepository.findById(blog.getApprovedBy()).orElse(null);
        }

        return BlogResponse.builder()
                .blogId(blog.getBlogId())
                .authorId(blog.getAuthor().getId())
                .authorName(blog.getAuthor().getFullName())
                .title(blog.getTitle())
                .content(blog.getContent())
                .coverImage(blog.getCoverImage())
                .images(blog.getImages())
                .tags(blog.getTags())
                .status(blog.getStatus())
                .viewCount(blog.getViewCount())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .approvedBy(blog.getApprovedBy())
                .approvedAt(blog.getApprovedAt())
                .approverName(approver != null ? approver.getFullName() : null)
                .build();
    }

    @Override
    public void approveBlog(UUID blogId, UUID managerId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        blog.setStatus("published");
        blog.setUpdatedAt(LocalDateTime.now());
        blog.setApprovedBy(managerId);
        blog.setApprovedAt(LocalDateTime.now());
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
            throw new RuntimeException("Từ chối truy cập: chỉ tác giả hoặc quản lý có thể xóa");
        }

        blogRepository.delete(blog);
    }

    @Override
    public BlogResponse createBlogWithAuth(String authToken, BlogRequest request) {
        User user = getUserFromToken(authToken);
        // Mặc định blog mới là "pending" để chờ duyệt
        request.setStatus("pending");
        return create(request, user.getEmail());
    }

    @Override
    public BlogResponse getBlogByIdWithAuth(String authToken, UUID id) {
        BlogResponse blog = getById(id);

        // Chỉ có thể xem blog đã xuất bản hoặc nếu là tác giả hoặc quản lý
        if (!"published".equalsIgnoreCase(blog.getStatus())) {
            User user = getUserFromToken(authToken);
            boolean isAuthor = blog.getAuthorId().equals(user.getId());
            Role role = roleRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            boolean isManager = role.getRoleLevel() == 3;

            if (!isAuthor && !isManager) {
                throw new RuntimeException("Từ chối truy cập: chỉ tác giả hoặc quản lý có thể xem blog này");
            }
        } else {
            // Tăng lượt xem cho blog đã xuất bản
            String trackingKey = id + ":" + authToken.substring(0, Math.min(10, authToken.length()));
            long currentTime = System.currentTimeMillis();
            Long lastViewTime = viewTracking.get(trackingKey);

            if (lastViewTime == null || currentTime - lastViewTime > VIEW_EXPIRY_TIME) {
                incrementViewCount(id);
                viewTracking.put(trackingKey, currentTime);
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
            throw new RuntimeException("Token xác thực không hợp lệ");
        }

        String token = authToken.substring(7);
        String userId = jwtUtil.extractUserId(token);
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
