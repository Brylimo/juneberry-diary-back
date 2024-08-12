package com.thxpapa.juneberrydiary.domain.blog;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="blog", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "blogId", callSuper=false)
@ToString
public class Blog extends BaseEntity {
    @Id
    @Comment("블로그 아이디")
    private String blogId;

    @Comment("블로그 이름")
    @Column(name="blog_name")
    private String blogName;

    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    private List<BlogUser> blogUsers = new ArrayList<BlogUser>();

    @Builder
    public Blog(String blogId, String blogName) {
        this.blogId = blogId;
        this.blogName = blogName;
    }
}