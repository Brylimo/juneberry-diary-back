package com.thxpapa.juneberrydiary.domain.post;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name="post_category", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "postCategoryUid", callSuper=false)
@ToString(of = {"postCategoryUid"})
public class PostCategory {
    @Id
    @Column(name="post_category_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCategoryUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    public void changePost(Post post) {
        this.post = post;
    }
    public void changeCategory(Category category) {
        this.category = category;
    }

    @Builder
    public PostCategory(Post post, Category category) {
        if (post != null) {
            changePost(post);
        }

        if (category != null) {
            changeCategory(category);
        }
    }
}