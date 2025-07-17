package com.suzumiya.blog.repository;

import com.suzumiya.blog.entity.Article;
import com.suzumiya.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Spring Data JPA 的魔法：根据方法名自动生成查询
    // 查找某个用户的所有文章
    List<Article> findByAuthor(User author);

    // 查找包含某个标题的所有文章
    List<Article> findByTitleContaining(String title);
}
