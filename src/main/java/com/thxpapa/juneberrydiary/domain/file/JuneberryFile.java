package com.thxpapa.juneberrydiary.domain.file;

import com.thxpapa.juneberrydiary.domain.BaseEntity;
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
    private int juneberryFileUid;

    @Comment("업로드 파일 이름")
    @Column(name="upload_name", length = 255)
    private String uploadName;

    @Comment("저장된 파일 이름")
    @Column(name="store_name", length = 255)
    private String storeName;

    @Comment("상태정보")
    @Column(name="status_cd", length = 3, nullable = false)
    @ColumnDefault("'01'")
    private String statusCd;

    @Builder
    public JuneberryFile(String uploadName, String storeName, String statusCd) {
        this.uploadName = uploadName;
        this.storeName = storeName;
        this.statusCd = statusCd;
    }
}
