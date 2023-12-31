package com.example.BankProject.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode
public class User extends AuditingFields {

    @Id
    @Column(length = 50)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String userPassword;

    @Setter
    @Column(length = 100)
    private String email;

    @Setter
    @Column(length = 100)
    private String nickname;

    @Setter
    private String memo;

    protected User(){}

    private User(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;

    }

    public static User of(String userId, String userPassword, String email, String nickName, String memo) {
        return new User(userId, userPassword,email, nickName,memo,null);
    }
    public static User of(String userId, String userPassword, String email, String nickName, String memo, String createdBy) {
        return new User(userId, userPassword,email, nickName,memo,createdBy);
    }


}
