package com.thxpapa.juneberrydiary.domain.blog;

import com.thxpapa.juneberrydiary.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="sub_category", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"name", "category"}, callSuper=false)
@ToString(of = {"subCategoryUid", "name"})
public class SubCategory {
    @Id
    @Column(name="sub_category_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryUid;

    @Comment("서브 카테고리 이름")
    @Column(name="name")
    private String name;

    @Comment("위치")
    @Column(name="position")
    private Integer position = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="categoryId")
    private Category category;

    @OneToMany(mappedBy = "subCategory")
    private List<Post> posts = new ArrayList<Post>();

    @Builder
    public SubCategory(String name, int position, Category category) {
        this.name = name;
        this.position = position;
        this.category = category;
    }
}