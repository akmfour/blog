package com.suzumiya.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(exclude = {"password", "articles", "roles"}) // 在 ToString 中排除密碼和集合，防止日誌洩漏和無限迴圈
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;


    // --- JWT ---

    @Column(length = 512) // 長度要足夠長
    private String refreshToken;

    // --- 關聯關係 (Relationships) ---

    // 刪除使用者時，不應級聯刪除文章
    @OneToMany(
            mappedBy = "author",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, // 移除 CascadeType.REMOVE
            fetch = FetchType.LAZY
    )
    private Set<Article> articles = new HashSet<>(); // 建議用 Set

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // --- 時間戳記 ---
    @CreationTimestamp // 使用 Hibernate 的註解自動生成時間
    @Column(nullable = false, updatable = false)
    private LocalDateTime registerDate;


    // --- Equals & HashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        // 基於唯一的 username 進行比較
        return userId != null && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 將 Set<Role> 轉換為 Collection<GrantedAuthority>
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        // 用 email 來做為登入的「用戶名」
        return this.email;
    }

    public String getUserName() {
        // 实际的用戶名
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 或者根據你的業務邏輯返回資料庫中的欄位值
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
