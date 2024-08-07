package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="post_tag", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "postUid", callSuper=false)
@ToString
public class PostTag extends BaseEntity {
    @Id
    @Column(name="post_tag_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postTagUid;

    @Comment("태그 이름")
    @Column(name="name")
    private String name;

    @Builder
    public PostTag(String name) {
        this.name = name;
    }
}
