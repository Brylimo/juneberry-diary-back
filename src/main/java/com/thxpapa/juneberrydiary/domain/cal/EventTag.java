package com.thxpapa.juneberrydiary.domain.cal;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="event_tag", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "eventTagUid", callSuper=false)
@ToString
public class EventTag extends BaseEntity {
    @Id
    @Column(name="event_tag_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventTagUid;

    @Comment("이벤트")
    @Column(name="event", length = 45, nullable = false)
    private String event;

    @Comment("상태정보")
    @Column(name="status_cd", length = 3, nullable = false)
    @ColumnDefault("'01'")
    private String statusCd;

    @ManyToOne
    @JoinColumn(name="dayId")
    private Day day;

    @Builder
    public EventTag(String event, Day day, String statusCd) {
        this.event = event;
        this.statusCd = statusCd;
        this.day = day;
    }
}
