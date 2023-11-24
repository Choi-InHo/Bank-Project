package com.example.BankProject.dto;

import com.example.BankProject.domain.ArticleComment;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.BankProject.domain.ArticleComment}
 */

public record ArticleCommentDto (
    LocalDateTime createdAt,
    UserDto userDto,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy,
    Long id,
    Long articleId,
    String content

){

    public ArticleCommentDto of(Long articleId, UserDto userDto, String content) {
        return new ArticleCommentDto(null, userDto, null, null, null, null, articleId, content);
    }


    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getCreatedAt(),
                UserDto.from(entity.getUser()),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                entity.getId(),
                entity.getArticle().getId(),
                entity.getContent()
        );
    }
}