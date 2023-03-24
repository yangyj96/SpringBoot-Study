package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;

public interface BoardService {
    Long register(BoardDTO dto);
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); //목록 처리
    BoardDTO get(Long bno);
    
    void removeWithReplies(Long bno); //삭제 기능

    void modify(BoardDTO boardDTO); // 수정기능
    default Board dtoToEntity(BoardDTO dto) {
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }
    //BoardService 인터페이스에 추가하는 entityToDTO()
    default  BoardDTO entityToDTO(Board board, Member member, Long replyCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerName(member.getName())
                .writerEmail(member.getEmail())
                .replyCount(replyCount.intValue()) //long 으로 나오므로 int로 처리하도록
                .build();
        return  boardDTO;
    }
}
