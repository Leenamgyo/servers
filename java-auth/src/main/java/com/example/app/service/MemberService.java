package com.example.app.service;

import com.example.app.domain.Member;
import com.example.app.dto.LoginDto;
import com.example.app.dto.MemberRequestDto;
import com.example.app.dto.TokenDto;
import com.example.app.config.jwt.JwtProvider;
import com.example.app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void signup(MemberRequestDto requestDto) {
        if (memberRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new RuntimeException("Already registered user.");
        }

        Member member = requestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = jwtProvider.createAccessToken(authentication);
        String refreshToken = jwtProvider.createRefreshToken(authentication);

        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                7,
                TimeUnit.DAYS
        );

        return new TokenDto("Bearer", accessToken, refreshToken);
    }

    @Transactional
    public TokenDto reissue(String refreshToken) {
        jwtProvider.validateToken(refreshToken);

        Authentication authentication = jwtProvider.getAuthentication(refreshToken);

        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (!refreshToken.equals(redisRefreshToken)) {
            throw new RuntimeException("Invalid refresh token.");
        }

        String accessToken = jwtProvider.createAccessToken(authentication);
        String newRefreshToken = jwtProvider.createRefreshToken(authentication);

        redisTemplate.opsForValue().set(
                authentication.getName(),
                newRefreshToken,
                7,
                TimeUnit.DAYS
        );

        return new TokenDto("Bearer", accessToken, newRefreshToken);
    }

    public void logout(String refreshToken) {
        jwtProvider.validateToken(refreshToken);

        Authentication authentication = jwtProvider.getAuthentication(refreshToken);

        redisTemplate.delete(authentication.getName());
    }
}
