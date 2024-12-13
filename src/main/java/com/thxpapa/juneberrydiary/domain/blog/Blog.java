package com.thxpapa.juneberrydiary.domain.blog;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="blog", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "blogId", callSuper=false)
@ToString(of = {"blogId", "blogName", "intro"})
public class Blog extends BaseEntity implements Persistable<String> {
    @Id
    @Comment("블로그 아이디")
    private String blogId;

    @Comment("블로그 이름")
    @Column(name="blog_name")
    private String blogName;

    @Lob
    @Comment("블로그 소개")
    @Column(name="intro", nullable = true)
    private String intro;

    @Comment("포스트 idx 개수")
    @Column(name="post_idx_cnt")
    private long postIdxCnt;

    @OneToMany(mappedBy = "blog")
    private List<BlogUser> blogUsers = new ArrayList<BlogUser>();

    @OneToMany(mappedBy = "blog")
    private List<Category> categories = new ArrayList<Category>();

    @Override
    public String getId() {
        return blogId;
    }

    @Override
    public boolean isNew() { // merge 사용 방지
        return getRegDt() == null;
    }

    public void updatePostIdxCnt(long postIdxCnt) {
        this.postIdxCnt = postIdxCnt;
    }

    @Builder
    public Blog(String blogId, String blogName, String intro, long postIdxCnt) {
        this.blogId = blogId;
        this.blogName = blogName;
        this.intro = intro;
        this.postIdxCnt = postIdxCnt;
    }
}