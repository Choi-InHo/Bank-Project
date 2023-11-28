//package com.example.BankProject.config;
//
//
//import com.example.BankProject.dto.UserDto;
//import com.example.BankProject.service.UserService;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.event.annotation.BeforeTestMethod;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//
//@Import(SecurityConfig.class)
//public class TestSecurityConfig {
//
//    @MockBean private UserService UserService;
//
//    @BeforeTestMethod
//    public void securitySetUp() {
//        given(UserService.searchUser(anyString()))
//                .willReturn(Optional.of(createUserDto()));
//        given(UserService.saveUser(anyString(), anyString(), anyString(), anyString(), anyString()))
//                .willReturn(createUserDto());
//    }
//
//
//    private UserDto createUserDto() {
//        return UserDto.of(
//                "unoTest",
//                "pw",
//                "uno-test@email.com",
//                "uno-test",
//                "test memo"
//        );
//    }
//
//}