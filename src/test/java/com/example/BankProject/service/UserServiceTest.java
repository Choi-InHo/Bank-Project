//package com.example.BankProject.service;
//
//
//import com.example.BankProject.domain.User;
//import com.example.BankProject.dto.UserDto;
//import com.example.BankProject.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//@DisplayName("비즈니스 로직 - 회원")
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @InjectMocks private UserService sut;
//
//    @Mock private UserRepository UserRepository;
//
//    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
//    @Test
//    void givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {
//        // Given
//        String username = "uno";
//        given(UserRepository.findById(username)).willReturn(Optional.of(createUser(username)));
//
//        // When
//        Optional<UserDto> result = sut.searchUser(username);
//
//        // Then
//        assertThat(result).isPresent();
//        then(UserRepository).should().findById(username);
//    }
//
//    @DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는 Optional을 반환한다.")
//    @Test
//    void givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
//        // Given
//        String username = "wrong-user";
//        given(UserRepository.findById(username)).willReturn(Optional.empty());
//
//        // When
//        Optional<UserDto> result = sut.searchUser(username);
//
//        // Then
//        assertThat(result).isEmpty();
//        then(UserRepository).should().findById(username);
//    }
//
//    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
//    @Test
//    void givenUserParams_whenSaving_thenSavesUser() {
//        // Given
//        User User = createUser("uno");
//        User savedUser = createSigningUpUser("uno");
//        given(UserRepository.save(User)).willReturn(savedUser);
//
//        // When
//        UserDto result = sut.saveUser(
//                User.getUserId(),
//                User.getUserPassword(),
//                User.getEmail(),
//                User.getNickname(),
//                User.getMemo()
//        );
//
//        // Then
//        assertThat(result)
//                .hasFieldOrPropertyWithValue("userId", User.getUserId())
//                .hasFieldOrPropertyWithValue("userPassword", User.getUserPassword())
//                .hasFieldOrPropertyWithValue("email", User.getEmail())
//                .hasFieldOrPropertyWithValue("nickname", User.getNickname())
//                .hasFieldOrPropertyWithValue("memo", User.getMemo())
//                .hasFieldOrPropertyWithValue("createdBy", User.getUserId())
//                .hasFieldOrPropertyWithValue("modifiedBy", User.getUserId());
//        then(UserRepository).should().save(User);
//    }
//
//
//    private User createUser(String username) {
//        return createUser(username, null);
//    }
//
//    private User createSigningUpUser(String username) {
//        return createUser(username, username);
//    }
//
//    private User createUser(String username, String createdBy) {
//        return User.of(
//                username,
//                "password",
//                "e@mail.com",
//                "nickname",
//                "memo",
//                createdBy
//        );
//    }
//
//}