package org.zerock.mreview.dto;

import lombok.*;
import org.zerock.mreview.entity.Movie;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long mno;
    
    private String title;
    
    // 게시판 삭제 여부
    private String delYn;

    // 빌더 패턴을 통해 인스턴스를 만들 때 특정 필드를 특정 값으로 초기화하고 싶을떄 @Builder.Default 사용
    @Builder.Default
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();

    // 영화의 평균 평점
    private double avg;

    // 리뷰 수 jpa의 count()
    private int reviewCnt;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

}


