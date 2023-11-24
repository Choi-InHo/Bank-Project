package com.example.BankProject.controller;


import com.example.BankProject.domain.constant.SearchType;
import com.example.BankProject.dto.response.ArticleResponse;
import com.example.BankProject.service.ArticleService;
import com.example.BankProject.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static java.time.LocalDateTime.now;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;


    @GetMapping("/articles")
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model map) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue,pageable).map(ArticleResponse::from);

        map.addAttribute("articles", articles);

        return "articles/index";

    }

//    @GetMapping("/a")
//    public String article(Model map){
//        people article = new people("dsa","man");
//        map.addAttribute("a",article);
//        return "articles/index";
//    }

}
