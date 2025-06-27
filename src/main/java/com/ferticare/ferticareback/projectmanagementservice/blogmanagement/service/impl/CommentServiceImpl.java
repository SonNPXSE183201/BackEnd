package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.entity.Blog;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.repository.BlogRepository;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.CommentRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.entity.Comment;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.repository.CommentRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CommentServiceImpl {

    private final CommentRepository commentRepo;
    private final BlogRepository blogRepo;
    private final UserRepository userRepo;

    public CommentServiceImpl(CommentRepository commentRepo,
                              BlogRepository blogRepo,
                              UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.blogRepo = blogRepo;
        this.userRepo = userRepo;
    }

    // Tạo mới bình luận
    public Comment createComment(UUID userId, CommentRequest req) {
        Blog blog = blogRepo.findById(req.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setUser(user);
        comment.setContent(req.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setIsVisible(true);

        // Nếu có parentId thì là reply comment
        if (req.getParentId() != null) {
            Comment parent = commentRepo.findById(req.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParent(parent);
        }

        return commentRepo.save(comment);
    }

    // Lấy tất cả comment hiển thị cho blog
    public List<Comment> getVisibleCommentsByBlog(UUID blogId) {
        return commentRepo.findByBlogBlogIdAndIsVisibleTrueOrderByCreatedAtAsc(blogId);
    }

    // Ẩn comment (chỉ chủ comment hoặc admin mới được phép)
    public void hideComment(UUID commentId, UUID userId, boolean isAdmin) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        boolean isOwner = comment.getUser().getId().equals(userId);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to hide this comment.");
        }

        comment.setIsVisible(false);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepo.save(comment);
    }
}