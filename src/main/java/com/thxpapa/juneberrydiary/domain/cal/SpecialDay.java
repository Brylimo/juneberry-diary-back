package com.thxpapa.juneberrydiary.domain.cal;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;

@Getter
@Entity
@Table(name="special_day", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "specialDayUid", callSuper=false)
@ToString
public class SpecialDay extends BaseTimeEntity implements Persistable<String> {
    @Id
    @Column(name="special_day_uid")
    private String specialDayUid;

    @Comment("데이터셋 식별자")
    @Column(name="dat_st_id")
    private String datStId;

    @Comment("날짜")
    @Column(name="date")
    private LocalDate date;

    @Comment("이름")
    @Column(name="date_name")
    private String dateName;

    @Comment("공휴일 여부")
    @Column(name="holiday_cd")
    private Boolean holidayCd;

    static public final String idSplitter = "::";

    @Override
    public String getId() {
        return specialDayUid;
    }

    @Override
    public boolean isNew() { // merge 사용 방지
        return getRegDt() == null;
    }

    @Builder
    public SpecialDay(String specialDayUid, String datStId, LocalDate date, String dateName, Boolean holidayCd) {
        this.specialDayUid = specialDayUid;
        this.datStId = datStId;
        this.date = date;
        this.dateName = dateName;
        this.holidayCd = holidayCd;
    }
}