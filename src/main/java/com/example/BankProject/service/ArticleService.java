package com.example.BankProject.service;


import com.example.BankProject.domain.Article;
import com.example.BankProject.domain.Hashtag;
import com.example.BankProject.domain.User;
import com.example.BankProject.domain.constant.SearchType;
import com.example.BankProject.dto.ArticleDto;
import com.example.BankProject.dto.ArticleWithCommentsDto;
import com.example.BankProject.repository.ArticleRepository;
import com.example.BankProject.repository.HashtagRepository;
import com.example.BankProject.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleService {
    private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;
    private final HashtagService hashtagService;

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchType == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUser_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME ->
                    articleRepository.findByUser_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtagNames(
                            Arrays.stream(searchKeyword.split(" ")).toList(),
                            pageable
                    )
                    .map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다"));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));

    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            User user = userRepository.getReferenceById(dto.userDto().userId());

            if (article.getUser().equals(user)) {
                if (dto.title() != null) {
                    article.setTitle(dto.title());
                }
                if (dto.content() != null) {
                    article.setContent(dto.content());
                }

                Set<Long> hashtagIds = article.getHashtags().stream()
                        .map(Hashtag::getId)
                        .collect(Collectors.toUnmodifiableSet());
                article.clearHashtags();
                articleRepository.flush();

                hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

                Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());
                article.addHashtags(hashtags);
                //article.setHashtag(dto.hashtag);
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패");
        }


    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public void saveArticle(ArticleDto articleDto) {
        User user = userRepository.getReferenceById(articleDto.userDto().userId());
        Set<Hashtag> hashtags = renewHashtagsFromContent(articleDto.content());

        Article article = articleDto.toEntity(user);
        article.addHashtags(hashtags);
        articleRepository.save(article);

    }

    public void deleteArticle(long articleId, String userId) {
        Article article = articleRepository.getReferenceById(articleId);
        Set<Long> hashtagsIds = article.getHashtags().stream()
                .map(Hashtag::getId)
                .collect(Collectors.toUnmodifiableSet());

        articleRepository.deleteByIdAndUser_UserId(articleId, userId);
        articleRepository.flush();

        hashtagsIds.forEach(hashtagService::deleteHashtagWithoutArticles);
    }

    private Set<Hashtag> renewHashtagsFromContent(String content) {
        Set<String> hashtagNamesInContent = hashtagService.parseHashtagNames(content);
        Set<Hashtag> hashtags = hashtagService.findHashtagByNames(hashtagNamesInContent);
        Set<String> existingHashtagNames = hashtags.stream().
                map(Hashtag::getHashtagName)
                .collect(Collectors.toUnmodifiableSet());
        hashtagNamesInContent.forEach(newHashtagName -> {
            if (!existingHashtagNames.contains(newHashtagName)) {
                hashtags.add(Hashtag.of(newHashtagName));
            }
        });
        return hashtags;
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtagName, Pageable pageable){
        if(hashtagName == null || hashtagName.isBlank()){
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtagNames(List.of(hashtagName), pageable)
                .map(ArticleDto::from);
    }
    public List<String> getHashtags(){
        return hashtagRepository.findAllHashtagNames();
    }
}
