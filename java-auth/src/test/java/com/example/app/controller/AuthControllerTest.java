package com.example.app.controller;

import com.example.app.config.jwt.JwtProvider;
import com.example.app.dto.LoginDto;
import com.example.app.dto.MemberRequestDto;
import com.example.app.dto.TokenDto;
import com.example.app.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공 테스트")
    @WithMockUser
    void signupSuccess() throws Exception {
        // given
        MemberRequestDto requestDto = new MemberRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setPassword("password123");

        doNothing().when(memberService).signup(any(MemberRequestDto.class));

        // when & then
        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    @WithMockUser
    void loginSuccess() throws Exception {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password123");

        TokenDto tokenDto = new TokenDto("Bearer", "access-token", "refresh-token");
        given(memberService.login(any(LoginDto.class))).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    @DisplayName("토큰 재발급 테스트")
    @WithMockUser
    void reissueSuccess() throws Exception {
        // given
        String refreshToken = "old-refresh-token";
        TokenDto tokenDto = new TokenDto("Bearer", "new-access-token", "new-refresh-token");
        given(memberService.reissue(any())).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/auth/reissue")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    @DisplayName("로그아웃 테스트")
    @WithMockUser
    void logoutSuccess() throws Exception {
        // given
        String refreshToken = "refresh-token";
        doNothing().when(memberService).logout(any());

        // when & then
        mockMvc.perform(post("/auth/logout")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk());
    }
}
