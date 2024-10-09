package com.thxpapa.juneberrydiary.domain.geo;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="spot", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "spotUid", callSuper=false)
@ToString
public class Spot extends BaseEntity {
    @Id
    @Column(name="spot_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotUid;

    @Comment("장소이름")
    @Column(name="name", length = 45, nullable = true)
    private String name;

    @Comment("장소")
    @Column(name="loc", length = 40, nullable = false)
    private String loc;

    @Lob
    @Comment("장소설명")
    @Column(name="exp", nullable = true)
    private String exp;

    @Comment("경도")
    @Column(name="lon")
    private Double lon;

    @Comment("위도")
    @Column(name="lat")
    private Double lat;

    @Comment("상태정보")
    @Column(name="status_cd", length = 3, nullable = false)
    @ColumnDefault("'01'")
    private String statusCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="juneberryFileId")
    private JuneberryFile juneberryFile;

    @Builder
    public Spot(String name, String loc, String exp, Double lon, Double lat, JuneberryFile juneberryFile, String statusCd) {
        this.name = name;
        this.loc = loc;
        this.exp = exp;
        this.lon = lon;
        this.lat = lat;
        this.statusCd = statusCd;
        this.juneberryFile = juneberryFile;
    }
}
