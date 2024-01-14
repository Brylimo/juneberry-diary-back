package com.thxpapa.juneberrydiary.domain.cal;

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
public class EventTag {
    @Id
    @Column(name="event_tag_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventTagUid;

    @Comment("이벤트")
    @Column(name="event", length = 45, nullable = false)
    private String event;

    @Comment("순서")
    @Column(name="position")
    @ColumnDefault("0")
    private int position;

    @ManyToOne
    @JoinColumn(name="dayId")
    private Day day;

    @Builder
    public EventTag(String event, int position, Day day) {
        this.event = event;
        this.position = position;
        this.day = day;
    }
}
