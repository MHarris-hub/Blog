package com.programming.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private long id;
    private String title;
    private String content;
    private String username;
}
