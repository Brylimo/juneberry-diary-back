package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Entity
@Table(name="post", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "postUid", callSuper=false)
@ToString
public class Post {
    @Id
    @Column(name="post_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postUid;

    @Comment("날짜")
    @Column(name="date")
    private LocalDate date;

    @Comment("제목")
    @Column(name="title")
    private String title;

    @Comment("내용")
    @Column(name="content")
    private String content;

    @ManyToOne
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    @Builder
    public Post(LocalDate date, String title, String content, JuneberryUser juneberryUser) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.juneberryUser = juneberryUser;
    }
}