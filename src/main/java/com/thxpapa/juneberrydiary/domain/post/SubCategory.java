package com.thxpapa.juneberrydiary.domain.post;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="sub_category", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "subCategoryUid", callSuper=false)
@ToString(of = {"subCategoryUid", "name"})
public class SubCategory {
    @Id
    @Column(name="sub_category_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryUid;

    @Comment("서브 카테고리 이름")
    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;

    @Builder
    public SubCategory(String name) {
        this.name = name;
    }
}
