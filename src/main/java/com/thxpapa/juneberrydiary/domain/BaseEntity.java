package com.thxpapa.juneberrydiary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name="reg_dt")
    @Comment("등록 날짜 시간")
    private LocalDateTime regDt;

    @LastModifiedDate
    @Column(name="mod_dt")
    @Comment("업데이트 날짜 시간")
    private LocalDateTime modDt;
}
