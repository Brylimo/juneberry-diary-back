package com.thxpapa.juneberrydiary.domain.blog;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="category", schema="datamart", indexes = {
        @Index(name = "idx_blog_id", columnList = "blogId"),
        @Index(name = "idx_position", columnList = "position")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "name", callSuper = false) // 이름만 동일성 비교 기준
@ToString(of = {"categoryUid", "name"})
public class Category extends BaseTimeEntity {
    @Id
    @Column(name="category_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryUid;

    @Comment("카테고리 이름")
    @Column(name="name")
    private String name;

    @Comment("위치")
    @Column(name="position")
    private Integer position = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blogId")
    private Blog blog;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategory> subCategories = new ArrayList<SubCategory>();

    @Builder
    public Category(String name, int position, Blog blog) {
        this.name = name;
        this.position = position;
        this.blog = blog;
    }

    public void updateSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}