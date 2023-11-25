package com.example.BankProject.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Entity
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EqualsAndHashCode(callSuper = true)
public class Article extends AuditingFields{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(optional = false) // optional을 넣으면 User가 없는 게시글은 존재할 수 없다는 의미
    @Setter
    @JoinColumn(name = "userId")
    private User user;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false, length = 1000)
    private String content;

    protected Article() {}

    @ToString.Exclude
    @OrderBy("createdAt")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    private Article(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public static Article of(User user, String title, String content) {
        return new Article(user, title, content);
    }


}




