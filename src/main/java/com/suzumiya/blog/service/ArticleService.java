package com.suzumiya.blog.service;

import com.suzumiya.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ArticleService {

    public Optional<Article> findById(Long id);

    public Article saveArticle(Article article);

    Page<Article> findPublishedArticles(Pageable pageable);
}
