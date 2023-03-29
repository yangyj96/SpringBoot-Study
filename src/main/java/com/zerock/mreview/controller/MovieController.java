package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.service.MovieService;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/register")
    public void register(){

    }

    // 게시물 등록 처리
    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes){
        log.info("movieDTO: " + movieDTO);
        Long mno = movieService.register(movieDTO);

        // addFlashAttribute() : 단 한번만 데이터를 전달하는 용도로 사용한다
        // redirectAttributes : 한 번만 화면에서 "msg"라는 이름의 변수를 사용할 수 있도록 처리
        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("pageRequestDTO: " + pageRequestDTO);
        model.addAttribute("result", movieService.getList(pageRequestDTO));
    }

    @GetMapping({"/read", "/modify"})
    public void read(Integer mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("mno: " + mno);
        MovieDTO movieDTO = movieService.getMovie(mno);
        model.addAttribute("dto", movieDTO);
    }

    // 게시물 수정 처리
    @PostMapping("/modify")
    public String modify(MovieDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){

        movieService.modifyPost(dto);

        redirectAttributes.addFlashAttribute("page", requestDTO.getPage());
        redirectAttributes.addFlashAttribute("type",requestDTO.getType());
        redirectAttributes.addFlashAttribute("keyword",requestDTO.getKeyword());

        redirectAttributes.addFlashAttribute("mno",dto.getMno());

        return "redirect:/movie/list";
    }

    // 게시물 삭제 처리
    @PostMapping("/remove")
    public String remove(MovieDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){

        // POST 방식으로 mno 값을 전달하고 삭제 후,
        movieService.deletePost(dto.getMno());

        redirectAttributes.addFlashAttribute("msg", dto.getMno());

        // 다시 리스트 페이지로 이동
        return "redirect:/movie/list";
    }
}
