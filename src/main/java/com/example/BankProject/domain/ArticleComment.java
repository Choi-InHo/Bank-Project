package com.example.BankProject.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Optional;

@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArticleComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId") private User user;

    @Setter
    @Column(nullable = false, length = 100)
    private String content;

    @ManyToOne(optional = false)
    @Setter
    private Article article; // Article 의 id를 조회함

    protected ArticleComment(){}

    public ArticleComment(User user, String content, Article article) {
        this.user = user;
        this.content = content;
        this.article = article;
    }
}
