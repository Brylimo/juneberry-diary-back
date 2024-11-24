package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="tag", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "tagUid", callSuper=false)
@ToString(of = {"tagUid", "name"})
public class Tag extends BaseTimeEntity {
    @Id
    @Column(name="tag_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagUid;

    @Comment("태그 이름")
    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<PostTag> postTags = new ArrayList<PostTag>();

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}