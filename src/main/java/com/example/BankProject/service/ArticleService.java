package com.example.BankProject.service;


import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.User;
import com.example.BankProject.domain.constant.SearchType;
import com.example.BankProject.dto.ArticleDto;
import com.example.BankProject.dto.ArticleWithCommentsDto;
import com.example.BankProject.repository.ArticleRepository;
import com.example.BankProject.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleService {
    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchType == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }
        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword,pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUser_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUser_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다"));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow();
        return modelMapper.map(article, ArticleDto.class);

    }

    public void updateArticle(Long articleId, ArticleDto dto){
        try {
            Article article = articleRepository.findById(articleId).orElseThrow();
            User user = userRepository.getReferenceById(dto.userDto().userId());
            if(article.getUser().equals(user)){
            if (dto.title() != null) {
                article.setTitle(dto.title());
            }
            if (dto.content() != null) {
                article.setTitle(dto.content());
            }
            //article.setHashtag(dto.hashtag);
            }
        } catch(EntityNotFoundException e){
            log.warn("게시글 업데이트 실패");
        }


    }

    public long getArticleCount(){
        return articleRepository.count();
    }

    public void saveArticle(ArticleDto articleDto){
        Article article = modelMapper.map(articleDto, Article.class);
        articleRepository.save(article);
    }

    public void deleteArticle(long articleId, String userId){
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }


}
