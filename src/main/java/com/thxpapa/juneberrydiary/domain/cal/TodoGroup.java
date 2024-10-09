package com.thxpapa.juneberrydiary.domain.cal;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="todo_group", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "todoGroupUid", callSuper=false)
@ToString
public class TodoGroup extends BaseEntity {
    @Id
    @Column(name="todo_group_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoGroupUid;

    @Comment("그룹 이름")
    @Column(name="name", length = 45, nullable = false)
    private String name;

    @Comment("색깔")
    @Column(name="color", length = 45, nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="juneberryUserId")
    private JuneberryUser juneberryUser;

    @Builder
    public TodoGroup(String name, String color, JuneberryUser juneberryUser) {
        this.name = name;
        this.color = color;
        this.juneberryUser = juneberryUser;
    }
}
