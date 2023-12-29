package com.thxpapa.juneberrydiary.domain.score;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Entity
@Table(name="day", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "dayUid", callSuper=false)
@ToString
public class Day extends BaseEntity {
    @Id
    @Column(name="day_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dayUid;

    @Comment("날짜")
    @Column(name="date")
    private LocalDate date;

    @Comment("상태정보")
    @Column(name="status_cd", length = 3, nullable = false)
    @ColumnDefault("'01'")
    private String statusCd;

    @ManyToOne
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    @Builder
    public Day(LocalDate date, String statusCd) {
        this.date = date;
        this.statusCd = statusCd;
    }
}
