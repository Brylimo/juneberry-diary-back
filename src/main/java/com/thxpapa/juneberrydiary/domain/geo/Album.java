package com.thxpapa.juneberrydiary.domain.geo;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="album", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "albumUid", callSuper=false)
@ToString(of = {"albumUid", "name", "likeCnt", "exp"})
public class Album extends BaseEntity {
    @Id
    @Column(name="album_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumUid;

    @Comment("앨범이름")
    @Column(name="name", length = 45, nullable = true)
    private String name;

    @Comment("좋아요 개수")
    @Column(name="like_cnt")
    private int likeCnt = 0;

    @Lob
    @Comment("앨범설명")
    @Column(name="exp", nullable = true)
    private String exp;

    @Comment("상태정보")
    @Column(name="status_cd", length = 3, nullable = false)
    @ColumnDefault("'01'")
    private String statusCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="spotId")
    private Spot spot;

    @Builder
    public Album(String name, int likeCnt, String exp, String statusCd) {
        this.name = name;
        this.likeCnt = likeCnt;
        this.exp = exp;
        this.statusCd = statusCd;
    }
}
