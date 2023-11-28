package com.example.BankProject.dto;

import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.ArticleComment;
import com.example.BankProject.domain.User;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserDto userDto,
        Long parentCommentId,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static ArticleCommentDto of(Long articleId, UserDto userDto, String content) {
        return ArticleCommentDto.of(articleId, userDto, null, content);
    }

    public static ArticleCommentDto of(Long articleId, UserDto userDto, Long parentCommentId, String content) {
        return ArticleCommentDto.of(null, articleId, userDto, parentCommentId, content, null, null, null, null);
    }

    public static ArticleCommentDto of(Long id, Long articleId, UserDto userDto, Long parentCommentId, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleCommentDto(id, articleId, userDto, parentCommentId, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserDto.from(entity.getUser()),
                entity.getParentCommentId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public ArticleComment toEntity(Article article, User userAccount) {
        return ArticleComment.of(
                article,
                userAccount,
                content
        );
    }

}