package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import java.util.*;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository; //final

    private final MovieImageRepository imageRepository; //final     // MovieImageRepositor는 테이블 명과 매핑됨 (규칙)

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);      // dtoToEntity의 결과값을 entityMap에 넣어준다.
        Movie movie = (Movie) entityMap.get("movie");               // movie(key에 대한 value룰 가지고 왔음)
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);            // movie 데이터를 save() 메서드를 이용해 DB에 넣어줍니다.

        movieImageList.forEach(movieImage -> {      // forEach문법 찾아보기
            imageRepository.save(movieImage);
        });

        return movie.getMno();      // 결과값=리턴값=반환값
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
                (Movie) arr[0],
                (List<MovieImage>) (Arrays.asList((MovieImage) arr[1])),
                (Double) arr[2],
                (Long) arr[3])
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO getMovie(long mno) {

        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        Movie movie = (Movie) result.get(0)[0];     // Movie 엔티티는 가장 앞에 존재 - 모든 Row가 동일한 값

        List<MovieImage> movieImageList = new ArrayList<>();    // 영화의 이미지 개수만큼 MovieImage 객체 필요

        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2];     // 평균 평점
        Long reviewCnt = (Long) result.get(0)[3];   // 리뷰 개수

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }

    @Transactional
    @Override
    public void deletePost(long mno) {
        Movie movie = movieRepository.findById(mno).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. mno = " + mno));
        // orElseThrow 예외발생시킴
        movieRepository.deleteMovie(movie.getMno());
    }

    @Transactional
    @Override
    public void modifyPost(MovieDTO movieDTO){

        Movie movie = movieRepository.getOne(movieDTO.getMno());

        movie.changeTitle(movieDTO.getTitle());     // 제목

        movieRepository.save(movie);
    }

    @Override
    public MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt) {
        return MovieService.super.entitiesToDTO(movie, movieImages, avg, reviewCnt);
    }

    @Override
    public Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
        return MovieService.super.dtoToEntity(movieDTO);
    }
}


