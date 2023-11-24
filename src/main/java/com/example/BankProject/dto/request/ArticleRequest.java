package com.example.BankProject.dto.request;

import java.time.LocalDateTime;

public record ArticleRequest (
    Long id,
    String title,
    String content,
    LocalDateTime createdAt
){


}
