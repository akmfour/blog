package com.suzumiya.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ArticlePreviewDto {

    private Long articleId;
    private String title;
    private AuthorDto author;
    private LocalDate createTime;
    private Integer viewCount;
    private Integer commentCount;
    private List<String> tags;
}
