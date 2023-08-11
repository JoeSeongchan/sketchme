package com.dutaduta.sketchme.oidc.controller;

import com.dutaduta.sketchme.oidc.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private LoginService loginService;

    /**
     * access token 재발급 요청
     * @return
     */
    @PostMapping("/regenerate-token")
    public ResponseEntity<?> regenerateToken(HttpServletRequest request) {
        log.info("토큰 재발급");
        return loginService.regenerateToken(request).toEntity();
    }
}
