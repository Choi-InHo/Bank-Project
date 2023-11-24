package com.example.BankProject.dto;

import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.User;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.example.BankProject.domain.Article}
 */

public record ArticleDto (
    LocalDateTime createdAt,
    UserDto userDto,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy,
    Long id,
    String title,
    String content
){

    public static ArticleDto from(Article article) {
        return new ArticleDto(
                article.getCreatedAt(),
                UserDto.from(article.getUser()),
                article.getCreatedBy(),
                article.getModifiedAt(),
                article.getModifiedBy(),
                article.getId(),
                article.getTitle(),
                article.getContent()
        );
    }

    public Article toEntity(User user) {
        return Article.of(
                user,
                title,
                content
        );
    }
}