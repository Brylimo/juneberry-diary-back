package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name="post", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "postUid", callSuper=false)
@ToString(of = {"postUid", "date", "title", "description", "content", "isTemp", "isPublic"})
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "post_uid", updatable = false, nullable = false)
    private UUID postUid;

    @Comment("날짜")
    @Column(name="date")
    private LocalDate date;

    @Comment("제목")
    @Column(name="title")
    private String title;

    @Lob
    @Comment("대표 설명")
    @Column(name="description")
    private String description;

    @Lob
    @Comment("내용")
    @Column(name="content")
    private String content;

    @Comment("임시저장 여부")
    @Column(name="is_temp")
    private Boolean isTemp;

    @Comment("전체공개 여부")
    @Column(name="is_public")
    private Boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blogId")
    private Blog blog;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="thumbnailId")
    private JuneberryFile juneberryFile;

    @Builder
    public Post(LocalDate date, String title, String description, String content, Boolean isTemp, Boolean isPublic, JuneberryUser juneberryUser, Blog blog, JuneberryFile juneberryFile) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.content = content;
        this.isTemp = isTemp;
        this.isPublic = isPublic;
        this.juneberryUser = juneberryUser;
        this.blog = blog;
        this.juneberryFile = juneberryFile;
    }

    public Post updatePostByWritePost(PostRequestDto.WritePost writePost, JuneberryFile thumbnailFile) {
        if (thumbnailFile != null) {
            this.juneberryFile = thumbnailFile;
        } else if (writePost.getThumbnailPath() == null) {
            this.juneberryFile = null;
        }
        this.title = writePost.getTitle();
        this.description = writePost.getDescription();
        this.content = writePost.getContent();
        this.isTemp = writePost.getIsTemp();
        this.isPublic = writePost.getIsPublic();
        return this;
    }
}