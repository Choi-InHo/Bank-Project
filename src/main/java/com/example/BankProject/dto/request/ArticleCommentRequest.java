package com.example.BankProject.dto.request;

import com.example.BankProject.domain.Article;
import com.example.BankProject.dto.ArticleCommentDto;
import com.example.BankProject.dto.UserDto;

public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content){
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserDto userDto){
        return ArticleCommentDto.of(
                articleId,
                userDto,
                content
        );
    }

}
