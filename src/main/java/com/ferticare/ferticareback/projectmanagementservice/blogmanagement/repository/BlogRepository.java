package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.blogmanagement.entity.Blog;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BlogRepository extends JpaRepository<Blog, UUID> {
    List<Blog> findByStatus(String status);
    List<Blog> findByAuthor(User author);
}
