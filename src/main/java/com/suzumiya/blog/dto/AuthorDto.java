package com.suzumiya.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    private Long userId;
    private String userName;
    //image
}
