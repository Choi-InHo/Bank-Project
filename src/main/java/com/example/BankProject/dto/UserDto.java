package com.example.BankProject.dto;

import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.User;

import java.time.LocalDateTime;

public record UserDto(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy) {

    public static UserDto from(User entity) {
        return new UserDto(
                entity.getUserId(),
                entity.getUserPassword(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public User toEntity() {
        return User.of(
                userId,
                userPassword,
                email,
                nickname,
                memo
        );

    };
}
