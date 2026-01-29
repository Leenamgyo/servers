package com.example.app.controller;

import com.example.app.dto.LoginDto;
import com.example.app.dto.MemberRequestDto;
import com.example.app.dto.ReissueRequestDto;
import com.example.app.dto.TokenDto;
import com.example.app.dto.LogoutRequestDto;
import com.example.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody MemberRequestDto requestDto) {
        memberService.signup(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        return ResponseEntity.ok(memberService.reissue(reissueRequestDto.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        memberService.logout(logoutRequestDto.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
