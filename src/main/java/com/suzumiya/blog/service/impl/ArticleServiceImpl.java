package com.suzumiya.blog.service.impl;

import com.suzumiya.blog.entity.Article;
import com.suzumiya.blog.mapper.ArticleMapper;
import com.suzumiya.blog.repository.ArticleRepository;
import com.suzumiya.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public Optional<Article> findById(Long id) {
        // 直接调用Repository提供的方法
        return articleRepository.findById(id);
    }

    public Article saveArticle(Article article) {
        // save方法既可以用于新建，也可以用于更新
        return articleRepository.save(article);
    }

    public Page<Article> findPublishedArticles(Pageable pageable) {
        Page<Article> articlePage = articleRepository.findPublishedArticles(pageable);
        return articlePage.map(ArticleMapper::toPreviewDto);
    }
}
