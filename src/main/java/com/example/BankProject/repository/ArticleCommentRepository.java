package com.example.BankProject.repository;


import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.ArticleComment;
import com.example.BankProject.dto.ArticleCommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>{

    void deleteByIdAndUser_UserId(Long articleCommentId, String userId);

    List<ArticleComment> findByArticle_id(Long articleId);
}