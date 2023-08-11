package com.dutaduta.sketchme.oidc.controller;

import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import com.dutaduta.sketchme.oidc.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = JwtProvider.resolveToken(request);
        try {
            logoutService.logout(token);
            return ResponseFormat.success("로그아웃이 완료되었습니다.").toEntity();
        } catch (BusinessException e) {
            return ResponseFormat.fail(HttpStatus.BAD_REQUEST,"실패").toEntity();
        }
    }

    @DeleteMapping("user/signout")
    public ResponseEntity<ResponseFormat<String>> userSignout(HttpServletRequest request){
        String token = JwtProvider.resolveToken(request);
        Long userId = JwtProvider.getUserId(token, JwtProvider.getSecretKey());
        // 회원탈퇴 할 때도 로그아웃할 때의 로직은 그대로 수행해야 함
        logoutService.logout(token);
        // 회원탈퇴에 필요한 로직 추가로 수행
        logoutService.signout(userId);
        return ResponseFormat.success("회원탈퇴가 완료되었습니다.").toEntity();
    }
}
