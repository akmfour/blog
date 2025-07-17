package com.suzumiya.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"post", "author", "parent", "replies"}) // 避免關聯造成的無限迴圈
@NoArgsConstructor
@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comment_article_id", columnList = "article_id"),
        @Index(name = "idx_comment_status", columnList = "status")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId; // 主键ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // 一篇文章有多個留言，留言必須屬於一篇文章
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY) // 一個使用者可以有多個留言，但留言者可以是匿名，所以 optional
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY) // 自我關聯，指向父留言
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> replies = new HashSet<>();

    // --- 欄位 (Fields) ---

    @Column(nullable = false, length = 2000) // 移除 unique=true，並設定長度
    private String content;

    @Column(length = 50)
    private String guestName; // 重新命名以區分: 訪客的暱稱

    @Column(length = 100)
    private String guestEmail; // 訪客的Email

    @Enumerated(EnumType.STRING) // 使用 Enum 型別，並在資料庫中存儲為字串
    @Column(nullable = false, length = 20)
    private CommentStatus status = CommentStatus.PENDING; // 提供預設值

    @CreationTimestamp // 由 Hibernate 自動產生建立時間
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // 由 Hibernate 自動產生更新時間
    private LocalDateTime updatedAt;

    // --- Equals & HashCode ---
    // 為了在 Set 和 Map 中正確運作，並避免 JPA 代理問題，只基於 ID 進行比較

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId != null && Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // 持久化實體中，ID未賦值前，所有實體的 HashCode 應該一致
    }
}
