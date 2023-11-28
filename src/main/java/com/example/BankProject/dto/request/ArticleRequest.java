package com.example.BankProject.dto.request;

import com.example.BankProject.dto.ArticleDto;
import com.example.BankProject.dto.HashtagDto;
import com.example.BankProject.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticleRequest (
    String title,
    String content
){
    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    public ArticleDto toDto(UserDto userDto){
        return toDto(userDto, null);
    }

    public ArticleDto toDto(UserDto userDto, Set<HashtagDto> hashtagDtos) {
        return ArticleDto.of(
                userDto,
                title,
                content,
                hashtagDtos
        );
    }

}
