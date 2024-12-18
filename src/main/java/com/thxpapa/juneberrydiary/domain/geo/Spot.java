package com.thxpapa.juneberrydiary.domain.geo;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="spot", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "spotUid", callSuper=false)
@ToString(of = {"spotUid", "name", "loc", "exp", "lon", "lat"})
public class Spot extends BaseTimeEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="juneberryFileId")
    private JuneberryFile juneberryFile;

    public void changeJuneberryUser(JuneberryUser user) {
        this.juneberryUser = user;
        user.getSpots().add(this);
    }

    @Builder
    public Spot(String name, String loc, String exp, Double lon, Double lat, JuneberryFile juneberryFile, JuneberryUser user) {
        this.name = name;
        this.loc = loc;
        this.exp = exp;
        this.lon = lon;
        this.lat = lat;
        this.juneberryFile = juneberryFile;
        if (user != null) {
            changeJuneberryUser(user);
        }
    }
}
