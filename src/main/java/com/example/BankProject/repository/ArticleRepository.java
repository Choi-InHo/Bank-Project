package com.example.BankProject.repository;


import com.example.BankProject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends
        JpaRepository<Article, Long>{

}