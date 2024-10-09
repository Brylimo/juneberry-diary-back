package com.thxpapa.juneberrydiary.domain.blog;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name="blog_user", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "blogUserUid", callSuper=false)
@ToString(of = {"blogUserUid"})
public class BlogUser extends BaseEntity {
    @Id
    @Column(name="blog_user_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogUserUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blogId")
    private Blog blog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juneberryUserId")
    private JuneberryUser juneberryUser;

    public void changeBlog(Blog blog) {
        this.blog = blog;
        blog.getBlogUsers().add(this);
    }
    public void changeJuneberryUser(JuneberryUser user) {
        this.juneberryUser = user;
        user.getBlogUsers().add(this);
    }

    @Builder
    public BlogUser(Blog blog, JuneberryUser juneberryUser) {
        if (blog != null) {
            changeBlog(blog);
        }
        if (juneberryUser != null) {
            changeJuneberryUser(juneberryUser);
        }
    }
}