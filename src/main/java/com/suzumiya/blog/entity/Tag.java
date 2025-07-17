package com.suzumiya.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "articles") // 排除關聯欄位以避免無限迴圈
@NoArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(nullable = false, unique = true, length = 50)
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles = new HashSet<>();


    // --- 為了在 Set 中安全地運作，覆寫 equals 和 hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        // 在比較時，只考慮商業上唯一的欄位，如 tagName
        return Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        // 只基於商業上唯一的欄位計算 hashCode
        return Objects.hash(tagName);
    }
}
