package com.example.BankProject.service;


import com.example.BankProject.dto.ArticleCommentDto;
import com.example.BankProject.repository.ArticleCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ModelMapper modelMapper;


    public List<ArticleCommentDto> searchArticleComments(Long articleId) {

    }
    public void saveArticleComment(ArticleCommentDto) {

    }

    public void updateArticleComment(ArticleCommentDto dto) {

    }

    public void deleteArticleComment(Long articleCommentId, String userId) {

    }
}
