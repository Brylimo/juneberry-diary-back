package com.thxpapa.juneberrydiary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreationTimestamp
    @Column(name="reg_dt")
    @Comment("등록 날짜 시간")
    private LocalDateTime regDt;

    @UpdateTimestamp
    @Column(name="mod_dt")
    @Comment("업데이트 날짜 시간")
    private LocalDateTime modDt;
}
