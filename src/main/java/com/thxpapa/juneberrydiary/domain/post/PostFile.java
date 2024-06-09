package com.thxpapa.juneberrydiary.domain.post;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name="post_file", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "postFileUid", callSuper=false)
@ToString
public class PostFile extends BaseEntity {
    @Id
    @Column(name="post_file_uid")
    private int postFileUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juneberryFileId")
    private JuneberryFile juneberryFile;

    @Builder
    public PostFile(Post post, JuneberryFile juneberryFile) {
        this.post = post;
        this.juneberryFile = juneberryFile;
    }
}