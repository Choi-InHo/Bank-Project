package com.example.BankProject.repository;


import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>{

}