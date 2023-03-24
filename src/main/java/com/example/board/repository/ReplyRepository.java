package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    //Board 삭제시에 댓글을 삭제
    @Modifying
    @Query("delete from Reply r where r.board.bno =:bno ")
    void deleteByBno(Long bno);
    //게시물로 댓글 목록 사져오기
    List<Reply> getRepliesByBoardOrderByRno (Board board);
}
