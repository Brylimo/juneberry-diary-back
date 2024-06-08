package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name="post", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "postUid", callSuper=false)
@ToString
public class Post {
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

    @Comment("내용")
    @Column(name="content")
    private String content;

    @Comment("임시저장 여부")
    @Column(name="is_temp")
    private Boolean isTemp;

    @ManyToOne
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    @Builder
    public Post(LocalDate date, String title, String content, Boolean isTemp, JuneberryUser juneberryUser) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.isTemp = isTemp;
        this.juneberryUser = juneberryUser;
    }
}