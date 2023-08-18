package com.dutaduta.sketchme.oidc.service;

import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.oidc.domain.TokenObject;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class LogoutService {
    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;

    private final RedisTemplate<String, String> redisTemplate;

    public void logout(String token) {

        String secretKey = JwtProvider.getSecretKey();
        // Token 객체 내부에서 토큰의 상태 관리
        TokenObject tokenObject = new TokenObject(token);
        // access token에서 user id 가져옴
        Long userId = JwtProvider.getUserId(tokenObject.getToken(), secretKey);

        // 해당 user의 로그인 상태(is_logined) false로 바꾸기
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        user.updateIsLogined(false);

        // Redis에서 해당 user id로 저장된 refresh token 유무 확인 후 삭제
        if(redisTemplate.opsForValue().get(userId.toString()) != null) {
            redisTemplate.delete(userId.toString());
        }

        // access token 남은 유효시간 가지고 와서 redis에 저장
        Long remainTime = tokenObject.getRestTime();
        redisTemplate.opsForValue().set(tokenObject.getToken(), "logout", remainTime, TimeUnit.MILLISECONDS); // 유효 기간을 남은 시간만큼으로 설정

        // 로그아웃 완료
    }

    public void signout(Long userId) {
        // user is_deleted true로 변경
        User user = userRepository.getReferenceById(userId);
        if(user.isDeleted()) throw new BusinessException();
        user.updateIsDeleted(true);

        // artist도 is_deactivated, is_open 수정
        user.getArtist().deactivate();
        user.getArtist().updateIsOpen(false);
    }
}
