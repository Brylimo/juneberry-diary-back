package com.thxpapa.juneberrydiary.domain.file;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="juneberry_file", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "juneberryFileUid", callSuper=false)
@ToString
public class JuneberryFile extends BaseEntity {
    @Id
    @Column(name="juneberry_file_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long juneberryFileUid;

    @Comment("파일 타입")
    @Column(name="type")
    private String type;

    @Comment("파일 이름")
    @Column(name="name", length = 255)
    private String name;

    @Comment("파일 확장자")
    @Column(name="ext")
    private String ext;

    @Comment("파일 경로")
    @Column(name="path", length = 255)
    private String path;

    @Builder
    public JuneberryFile(String type, String name, String ext, String path) {
        this.type = type;
        this.name = name;
        this.ext = ext;
        this.path = path;
    }
}