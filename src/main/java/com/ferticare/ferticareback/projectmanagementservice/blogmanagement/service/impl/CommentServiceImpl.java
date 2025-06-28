package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.entity.Blog;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.repository.BlogRepository;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.CommentRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response.CommentResponse;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.entity.Comment;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.repository.CommentRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public CommentResponse createComment(UUID userId, CommentRequest req) {
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

        Comment savedComment = commentRepo.save(comment);
        return convertToResponse(savedComment);
    }

    // Lấy tất cả comment hiển thị cho blog
    public List<CommentResponse> getVisibleCommentsByBlog(UUID blogId) {
        return getVisibleCommentsByBlog(blogId, null, false);
    }

    // Lấy tất cả comment hiển thị cho blog với thông tin quyền
    public List<CommentResponse> getVisibleCommentsByBlog(UUID blogId, UUID currentUserId, boolean isAdmin) {
        List<Comment> comments = commentRepo.findByBlogBlogIdAndIsVisibleTrueOrderByCreatedAtAsc(blogId);
        return comments.stream()
                .filter(comment -> comment.getParent() == null) // Chỉ lấy comment gốc
                .map(comment -> convertToResponseWithReplies(comment, currentUserId, isAdmin))
                .collect(Collectors.toList());
    }

    // Ẩn comment (chỉ chủ comment hoặc admin mới được phép)
    public void hideComment(UUID commentId, UUID userId, boolean isAdmin) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        boolean isOwner = comment.getUser().getId().equals(userId);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to hide this comment.");
        }

        // Ẩn comment hiện tại
        comment.setIsVisible(false);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepo.save(comment);

        // Nếu là comment gốc (không có parent), ẩn tất cả comment con
        if (comment.getParent() == null) {
            hideAllReplies(comment);
        }
    }

    // Ẩn chỉ comment hiện tại (không ảnh hưởng đến comment con)
    public void hideCommentOnly(UUID commentId, UUID userId, boolean isAdmin) {
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

    // Ẩn tất cả comment con của một comment
    private void hideAllReplies(Comment parentComment) {
        List<Comment> replies = commentRepo.findByParentAndIsVisibleTrueOrderByCreatedAtAsc(parentComment);

        for (Comment reply : replies) {
            reply.setIsVisible(false);
            reply.setUpdatedAt(LocalDateTime.now());
            commentRepo.save(reply);

            // Đệ quy ẩn các comment con của reply (nếu có)
            hideAllReplies(reply);
        }
    }

    // Cập nhật comment (chỉ chủ comment mới được phép)
    public CommentResponse updateComment(UUID commentId, UUID userId, String newContent) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this comment.");
        }

        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        Comment savedComment = commentRepo.save(comment);
        return convertToResponse(savedComment);
    }

    // Convert Comment entity sang CommentResponse
    private CommentResponse convertToResponse(Comment comment) {
        return convertToResponse(comment, null, false);
    }

    // Convert Comment entity sang CommentResponse với thông tin quyền
    private CommentResponse convertToResponse(Comment comment, UUID currentUserId, boolean isAdmin) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setBlogId(comment.getBlog().getBlogId());
        response.setUserId(comment.getUser().getId());
        response.setUserName(comment.getUser().getFullName());
        response.setUserEmail(comment.getUser().getEmail());
        response.setContent(comment.getContent());
        response.setParentId(comment.getParent() != null ? comment.getParent().getCommentId() : null);
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setIsVisible(comment.getIsVisible());
        response.setReplyCount(0); // Sẽ tính sau

        // Kiểm tra quyền edit/delete
        boolean isOwner = currentUserId != null && comment.getUser().getId().equals(currentUserId);
        response.setCanEdit(isOwner);
        response.setCanDelete(isOwner || isAdmin);

        return response;
    }

    // Convert Comment với replies
    private CommentResponse convertToResponseWithReplies(Comment comment) {
        return convertToResponseWithReplies(comment, null, false);
    }

    // Convert Comment với replies và thông tin quyền
    private CommentResponse convertToResponseWithReplies(Comment comment, UUID currentUserId, boolean isAdmin) {
        CommentResponse response = convertToResponse(comment, currentUserId, isAdmin);

        // Lấy tất cả replies của comment này
        List<Comment> replies = commentRepo.findByParentAndIsVisibleTrueOrderByCreatedAtAsc(comment);
        List<CommentResponse> replyResponses = replies.stream()
                .map(reply -> convertToResponse(reply, currentUserId, isAdmin))
                .collect(Collectors.toList());

        response.setReplies(replyResponses);
        response.setReplyCount(replies.size());

        return response;
    }

    // Lấy số lượng comment của blog
    public int getCommentCountByBlog(UUID blogId) {
        List<Comment> comments = commentRepo.findByBlogBlogIdAndIsVisibleTrueOrderByCreatedAtAsc(blogId);
        return comments.size();
    }
}