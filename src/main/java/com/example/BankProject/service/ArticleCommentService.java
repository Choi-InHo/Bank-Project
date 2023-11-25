package com.example.BankProject.service;


import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.ArticleComment;
import com.example.BankProject.domain.User;
import com.example.BankProject.dto.ArticleCommentDto;
import com.example.BankProject.repository.ArticleCommentRepository;
import com.example.BankProject.repository.ArticleRepository;
import com.example.BankProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleCommentService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .toList();

    }
    public void saveArticleComment(ArticleCommentDto articleCommentDto) {
        try {
            Article article = articleRepository.getReferenceById(articleCommentDto.articleId());
            User user = userRepository.getReferenceById(articleCommentDto.userDto().userId());

            articleCommentRepository.save(articleCommentDto.toEntity(article, user));
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패");
        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try{
        ArticleComment articleComment = articleCommentRepository.findById(dto.id()).orElseThrow();
        User user = userRepository.getReferenceById(dto.userDto().userId());

        if(articleComment.getUser().equals(user)){
            if (dto.content() != null) {
                articleComment.setContent(dto.content());
            }
        }}
        catch(EntityNotFoundException e){
            log.warn("댓글 업데이트 실패");
        }

    }

    public void deleteArticleComment(long articleCommentId, String userId) {
        articleCommentRepository.deleteByIdAndUser_UserId(articleCommentId, userId);
    }
}
