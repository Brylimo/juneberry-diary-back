package com.thxpapa.juneberrydiary.domain.cal;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.dto.cal.TodoUpdateDto;
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
    private int todoUid;

    @Comment("위치")
    @Column(name="position")
    @ColumnDefault("0")
    private int position;

    @Comment("내용")
    @Column(name="content", length = 45, nullable = false)
    private String content;

    @Comment("보상")
    @Column(name="reward")
    @ColumnDefault("0")
    private int reward;

    @Comment("완료 유무")
    @Column(name="done_cd")
    @ColumnDefault("false")
    private boolean doneCd;

    @Comment("상태정보")
    @Column(name="status_cd", length = 3, nullable = false)
    @ColumnDefault("'01'")
    private String statusCd;

    @ManyToOne
    @JoinColumn(name="dayId")
    private Day day;

    @ManyToOne
    @JoinColumn(name="todoGroupId")
    private TodoGroup todoGroup;

    @Builder
    public Todo(String content, int position, int reward, Day day, TodoGroup todoGroup, String statusCd) {
        this.content = content;
        this.position = position;
        this.reward = reward;
        this.statusCd = statusCd;
        this.day = day;
        this.todoGroup = todoGroup;
    }

    public Todo updateTodo(EntityManager em, TodoUpdateDto todoUpdateDto) {
        if (todoUpdateDto.getContent() != null) {
            this.content = todoUpdateDto.getContent();
        }
        if (todoUpdateDto.getReward() != null) {
            this.reward = todoUpdateDto.getReward();
        }
        if (todoUpdateDto.getDoneCd() != null) {
            this.doneCd = todoUpdateDto.getDoneCd();
        }

        em.merge(this);
        return this;
    }
}
