package com.suzumiya.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"password", "articles", "roles"}) // 在 ToString 中排除密碼和集合，防止日誌洩漏和無限迴圈
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false, length = 255) // 長度應足以儲存加密後的 hash
    private String password;

    // --- 關聯關係 (Relationships) ---

    // 刪除使用者時，不應級聯刪除文章
    @OneToMany(
            mappedBy = "author",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, // 移除 CascadeType.REMOVE
            fetch = FetchType.LAZY
    )
    private Set<Article> articles = new HashSet<>(); // 建議用 Set

    // (進階建議) 使用者角色關聯
    @ManyToMany(fetch = FetchType.EAGER) // 權限資訊通常建議立即載入
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    // --- 時間戳記 ---
    @CreationTimestamp // 使用 Hibernate 的註解自動生成時間
    @Column(nullable = false, updatable = false)
    private LocalDateTime registerDate; // **重要：使用 LocalDateTime**


    // --- Equals & HashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        // 基於唯一的 username 進行比較
            return userName != null && Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
