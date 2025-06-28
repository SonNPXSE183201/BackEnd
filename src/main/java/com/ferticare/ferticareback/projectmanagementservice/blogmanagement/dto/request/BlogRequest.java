package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlogRequest {
    private String title;
    private String content;
    private String coverImage;
    private List<String> images = new ArrayList<>();
    private String tags;
    private String status;
}