package com.suzumiya.blog.service;

import com.suzumiya.blog.entity.Article;
import com.suzumiya.blog.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Optional<Article> findById(Long id) {
        // 直接调用Repository提供的方法
        return articleRepository.findById(id);
    }

    public Article saveArticle(Article article) {
        // save方法既可以用于新建，也可以用于更新
        return articleRepository.save(article);
    }

}
