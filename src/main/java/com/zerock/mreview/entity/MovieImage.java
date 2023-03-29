package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

// 나중에 사용할 이미지에 대한 정보 기록
@Entity
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "movie")        // ToString()는  객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메소드
public class MovieImage {

    // 기본 키 매핑
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)        // 데이터를 저장함과 동시에 생성된 기본 키 값을 얻어 올 수 있음
    private Long inum;

    // java.util.UUID를 이용해 고유 번호 생성
    private String uuid;

    private String imgName;

    private String path;

    // movie 테이블이 PK를 가지고 movie_image 테이블이 FK를 가지므로 movie_image에 @ManyToOne 적용
    @ManyToOne(fetch = FetchType.LAZY)      //무조건 lazy로!
    private Movie movie;
}
