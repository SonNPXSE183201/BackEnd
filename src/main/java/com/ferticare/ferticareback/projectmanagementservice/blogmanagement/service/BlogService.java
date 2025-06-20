package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request.BlogRequest;
import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.response.BlogResponse;

import java.util.List;
import java.util.UUID;

public interface BlogService {
    BlogResponse create(BlogRequest request, String authorEmail);
    BlogResponse getById(UUID id);
    List<BlogResponse> getPublishedBlogs();
    List<BlogResponse> getAllBlogs();
    List<BlogResponse> getBlogsByUser(String email);
    void approveBlog(UUID blogId);
    void archiveBlog(UUID id, String email);
    void deleteBlog(UUID blogId, String email);

}