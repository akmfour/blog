package com.suzumiya.blog.mapper;

import com.suzumiya.blog.dto.ArticlePreviewDto;
import com.suzumiya.blog.dto.AuthorDto;
import com.suzumiya.blog.entity.Article;
import com.suzumiya.blog.entity.Tag;
import com.suzumiya.blog.entity.UserEntity;

import java.util.Collections;
import java.util.stream.Collectors;

public class ArticleMapper {

    public static ArticlePreviewDto toPreviewDto(Article article) {

        /**
         * 將 UserEntity 轉換為 AuthorDto
         * @param userEntity 資料庫實體物件
         * @return DTO 物件
         */
        UserEntity user = article.getAuthor();

        AuthorDto authorDto = AuthorDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .build();

        return ArticlePreviewDto.builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .author(authorDto)
                .viewCount(article.getViewCount())
                .commentCount(article.getComments().size() )
                .tags(article.getTags() != null ? article.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
}
