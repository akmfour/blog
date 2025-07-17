package com.suzumiya.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"author", "tags", "comments"}) // 排除關聯欄位
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class) // **重要：為了讓 @CreatedDate 生效**
@Table(name = "articles", indexes = {
        @Index(name = "idx_article_author_id", columnList = "user_id"),
        @Index(name = "idx_article_status", columnList = "status")
})
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition="TEXT")
    private String content;

    @Column(length = 512)
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // This creates a 'category_id' foreign key column in the 'articles' table
    private Category category;

    // --- 預設值欄位 ---
    @Column(nullable = false)
    private Integer viewCount = 0; // 提供預設值 0

    @Column(nullable = false)
    private Boolean allowComments = true; // 提供預設值 true

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ArticleStatus status = ArticleStatus.DRAFT;

    // --- 關聯關係 (Relationships) ---

    // 一篇文章必須有一個作者，所以 optional = false
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // 中間表
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    // 新增與 Comment 的關聯：一篇文章可以有多則留言
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();


    // --- 時間戳記 (Auditing) ---
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // --- Equals & HashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return articleId != null && Objects.equals(articleId, article.articleId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
