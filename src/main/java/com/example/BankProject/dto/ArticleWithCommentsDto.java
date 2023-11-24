package com.example.BankProject.dto;

import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.ArticleComment;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


public record ArticleWithCommentsDto (
    Long id,
    Set<ArticleCommentDto> articleCommentDtoSet,
    String content,
    String title,
    String createdBy,
    String modifiedBy,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
){


    public static ArticleWithCommentsDto of(Long id, Set<ArticleCommentDto> articleCommentDtos,  String title, String content, String modifiedBy, LocalDateTime modifiedAt, LocalDateTime createdAt, String createdBy) {
        return new ArticleWithCommentsDto(id, articleCommentDtos,  title, content, modifiedBy, createdBy,createdAt, modifiedAt);
    }

    public static ArticleWithCommentsDto from(Article entity) {
        return new ArticleWithCommentsDto(
                entity.getId(),
                entity.getArticleComments().stream()
                                .map(ArticleCommentDto::from)
                                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getContent(),
                entity.getTitle(),
                entity.getCreatedBy(),
                entity.getModifiedBy(),
                entity.getCreatedAt(),
                entity.getModifiedAt()

        );
    }
}
