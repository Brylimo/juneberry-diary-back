package com.thxpapa.juneberrydiary.domain.cal;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.dto.cal.CalRequestDto;
import com.thxpapa.juneberrydiary.enums.CheckStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="todo", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "todoUid", callSuper=false)
@ToString
public class Todo extends BaseEntity {
    @Id
    @Column(name="todo_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoUid;

    @Comment("위치")
    @Column(name="position")
    @ColumnDefault("0")
    private int position;

    @Comment("내용")
    @Column(name="content", length = 45, nullable = false)
    private String content;

    @Comment("보상")
    @Column(name="reward")
    private int reward = 0;

    @Comment("체크 값")
    @Column(name="chk_status")
    @Enumerated(EnumType.STRING)
    private CheckStatus chkStatus = CheckStatus.NONE;

    @ManyToOne
    @JoinColumn(name="dayId")
    private Day day;

    @ManyToOne
    @JoinColumn(name="todoGroupId")
    private TodoGroup todoGroup;

    @Builder
    public Todo(String content, int position, int reward, String chkStatus, Day day, TodoGroup todoGroup) {
        this.content = content;
        this.position = position;
        this.reward = reward;
        this.chkStatus = CheckStatus.fromValue(chkStatus);
        this.day = day;
        this.todoGroup = todoGroup;
    }

    public Todo updateTodoByTodoLine(CalRequestDto.TodoLine todoLine, TodoGroup todoGroup) {
        this.content = todoLine.getContent();
        this.todoGroup = todoGroup;
        return this;
    }

    public Todo updateTodoByCheck(CalRequestDto.TodoChk todoChk) {
        this.chkStatus = CheckStatus.fromValue(todoChk.getChkStatus());
        return this;
    }
}
