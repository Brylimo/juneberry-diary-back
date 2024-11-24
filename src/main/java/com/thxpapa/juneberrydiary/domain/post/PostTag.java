package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name="post_tag", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "postTagUid", callSuper=false)
@ToString(of = {"postTagUid"})
public class PostTag extends BaseTimeEntity {
    @Id
    @Column(name="post_tag_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId")
    private Tag tag;

    public void changePost(Post post) {
        this.post = post;
        post.getPostTags().add(this);
    }
    public void changeTag(Tag tag) {
        this.tag = tag;
        tag.getPostTags().add(this);
    }

    @Builder
    public PostTag(Post post, Tag tag) {
        if (post != null) {
            changePost(post);
        }

        if (tag != null) {
            changeTag(tag);
        }
    }
}