package org.zerock.mreview.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {     // abstract 추상

    @CreatedDate        // DB에 자동으로 넣어줌     // JPA에서 엔티티의 생성 시간을 처리
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate   // DB에 자동으로 넣어줌     // 최종 수정 시간을 자동으로 처리
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
