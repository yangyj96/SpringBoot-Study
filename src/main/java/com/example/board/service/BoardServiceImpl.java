package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository; // 자동 주입 final
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[])result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
     }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) { //삭제 기능 구현, 트랜젝션 추가
        //댓글 부터 삭제
        replyRepository.deleteByBno(bno);
        replyRepository.deleteById(bno);
    }
    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> optionalBoard = repository.findById(boardDTO.getBno());
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());
            repository.save(board);
        }
    }
    @Override
        public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()) );

        return new PageResultDTO<>(result,fn);
    }

}


