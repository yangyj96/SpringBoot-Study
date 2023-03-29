package org.zerock.mreview.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.*;

// 영화 제목만을 가지는 구조
// M:N 관계를 처리할 때는 반드시 맵핑 테이블의 설계는 마지막 단계에서 처리하고 '명사'에 해당하는 클래스를 먼저 설계
//        -> 영화(Movie)와 회원(Member)의 존재가 명사에 해당이므로 먼저 설계
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@DynamicInsert          // @DynamicInsert : 컬럼의 지정된 default 값을 적용시키며 INSERT할 때 사용
@ToString
public class Movie extends BaseEntity{

    @Id     // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK의 생성 규칙
    private Long mno;

    @Column
    private String title;

    private String delYn;     // 삭제 여부

    public void changeTitle(String title) {
        this.title = title;
    }

}
