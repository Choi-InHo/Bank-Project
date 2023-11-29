package com.example.BankProject.service;

import com.example.BankProject.domain.User;
import com.example.BankProject.dto.UserDto;
import com.example.BankProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<UserDto> searchUser(String username){
        return userRepository.findById(username)
                .map(UserDto::from);
    }

    public UserDto saveUser(String username, String password, String email, String nickname, String memo){
        return UserDto.from(
                userRepository.save(User.of(username, password, email, nickname, memo, username))
        );
    }
}
