package com.example.BankProject.config;

import com.example.BankProject.service.UserService;
import com.example.BankProject.dto.security.BoardPrincipal;
import com.example.BankProject.dto.security.KakaoOAuth2Response;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
    ) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        //카카오 이미지를 가져오기 위해 atCommonLocations()를 사용해줌, images파일로 만들어줘야해
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .antMatchers("/search").permitAll()
                        .mvcMatchers( //여기 있는 것들은 인증을 따로 해주지 않아도됨
                                HttpMethod.GET,
                                "/error",
                                "/",
                                "/articles",
                                "/articles/search-hashtag",
                                "/detect"
                        ).permitAll()
                        .anyRequest().authenticated() //그 외에 말한 것들은 인증을 해야한다.
                )
                .formLogin(withDefaults())// withDefaults 아무일도 하지 않고, and()를 없애주는 역할만 했음
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .csrf().disable()
                .oauth2Login(oAuth -> oAuth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .build();
    }
    //데이터베이스를 기반으로 인증 정보를 불러와다가 유저 정보를 리턴하는 메서드
    @Bean
    public UserDetailsService userDetailsService(UserService userAccountService) {
        return username -> userAccountService
                .searchUser(username)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserService userAccountService,
            PasswordEncoder passwordEncoder // 패스워드를 평문으로 넣으면 안됨
    ) { //한 번 사용하는 부분이어서 빈으로 등록을 따로 하지 않았음
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        //db에 카카오로부터 온 회원정보를 저장
        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            //application.yaml파일의 client.cilent-id를 가져옴
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.id()); //String.valueOf -> 스트링 타입으로 변환해줌
            String username = registrationId + "_" + providerId;
            //패스워드는 카카오API에 권한을 넘겨주지만, useraccount에 패스워드가 Notnull값이기 때문에 넣어준다.
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());

            return userAccountService.searchUser(username)
                    .map(BoardPrincipal::from)
                    .orElseGet(() -> //없을 경우 저장해준다.
                            BoardPrincipal.from(
                                    userAccountService.saveUser(
                                            username,
                                            dummyPassword,
                                            kakaoResponse.email(),
                                            kakaoResponse.nickname(),
                                            null
                                    )
                            )
                    );
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}