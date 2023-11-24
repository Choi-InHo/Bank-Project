package com.example.BankProject.dto.response;

import com.example.BankProject.dto.ArticleDto;

import java.time.LocalDateTime;

public record ArticleResponse (
    Long id,
    String title,
    String content,
    LocalDateTime createdAt
){

    public static ArticleResponse from(ArticleDto dto) {
        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.createdAt()
        );
    }
}
