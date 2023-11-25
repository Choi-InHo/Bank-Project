package com.example.BankProject.service;

import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.User;
import com.example.BankProject.domain.constant.SearchType;
import com.example.BankProject.dto.ArticleDto;
import com.example.BankProject.dto.ArticleWithCommentsDto;
import com.example.BankProject.repository.ArticleCommentRepository;
import com.example.BankProject.repository.ArticleRepository;
import com.example.BankProject.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @InjectMocks
    private ArticleService sut;
    @Mock
    ArticleRepository articleRepository;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지 전체를 반환한다.")
    void givenNoSearchParameters_whenSearchingArticles_thenReturnArticlePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
        // When
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);
        // Then

        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @Test
    @DisplayName("검색어와 함께 입력하면 , 게시글 페이지를 반환한다.")
    void givenSearchParameters_whenSearchingArticles_thenReturnArticlePage() {
        //Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        //When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        //Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("게시글을 ID로 조회하면, 댓글 담긴 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleWithComments_thenReturnsArticleWithComment() {
        //Given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        //When
        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);

        //Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent());
        then(articleRepository).should().findById(articleId);

    }

    private Article createArticle(){
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );
        ReflectionTestUtils.setField(article, "id", 1L);
        return article;
    }

    private User createUserAccount(){
        return User.of(
                "ino",
                "password",
                "aaa@aaaaaa",
                "inoo",
                null
        );

    }
}