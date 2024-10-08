package com.thxpapa.juneberrydiary.domain.cal;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    private Long dayUid;

    @Comment("날짜")
    @Column(name="date")
    private LocalDate date;

    @Comment("오늘의 이모지")
    @Column(name="emoji_codes")
    private String emojiCodes;

    @Comment("오늘의 텍스트")
    @Column(name="today_txt")
    private String todayTxt;

    @Comment("이벤트 태그 목록")
    @Column(name="event_tags", columnDefinition = "varchar(255) default ''")
    private String eventTags;

    @ManyToOne
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    public void updateTodayTxt(Optional<String> optTodayTxt) {
        optTodayTxt.ifPresent(todayTxt->{
            this.todayTxt = todayTxt;
        });
    }

    public void updateEmojiCodes(Optional<List<String>> optEmojiCodeList) {
        optEmojiCodeList.ifPresent(emojiCodeList->{
            this.emojiCodes = String.join(",", emojiCodeList);
        });
    }

    public void updateEventTagList(Optional<List<String>> optEventTagList) {
        optEventTagList.ifPresent(eventTagList->{
            this.eventTags = String.join(",", eventTagList);
        });
    }

    @Builder
    public Day(LocalDate date, String emojiCodes, String todayTxt, String eventTagList, JuneberryUser juneberryUser) {
        this.date = date;
        this.emojiCodes = emojiCodes;
        this.todayTxt = todayTxt;
        this.eventTags = eventTagList;
        this.juneberryUser = juneberryUser;
    }
}
