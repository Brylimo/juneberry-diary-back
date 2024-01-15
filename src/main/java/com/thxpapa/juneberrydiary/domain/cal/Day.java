package com.thxpapa.juneberrydiary.domain.cal;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Table(name="day", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "dayUid", callSuper=false)
@ToString
public class Day {
    @Id
    @Column(name="day_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dayUid;

    @Comment("날짜")
    @Column(name="date")
    private LocalDate date;

    @Comment("이벤트 태그 목록")
    @Column(name="event_tag_list")
    private String eventTagList;

    @ManyToOne
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    public void setEventTagList(List<String> eventTagList) {
        this.eventTagList = "hi";
    }

    @Builder
    public Day(LocalDate date, String eventTagList) {
        this.date = date;
        this.eventTagList = eventTagList;
    }
}
