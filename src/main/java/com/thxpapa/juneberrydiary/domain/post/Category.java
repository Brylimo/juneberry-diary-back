package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="category", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "categoryUid", callSuper=false)
@ToString(of = {"categoryUid", "name"})
public class Category extends BaseTimeEntity {
    @Id
    @Column(name="category_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryUid;

    @Comment("카테고리 이름")
    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<PostCategory> postCategories = new ArrayList<PostCategory>();

    @Builder
    public Category(String name) {
        this.name = name;
    }
}