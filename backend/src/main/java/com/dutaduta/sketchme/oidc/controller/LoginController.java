package com.dutaduta.sketchme.oidc.controller;

import com.dutaduta.sketchme.oidc.dto.TokenResponseDTO;
import com.dutaduta.sketchme.oidc.service.KakaoService;
import com.dutaduta.sketchme.oidc.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Log4j2
@RestController
public class LoginController {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    @Autowired
    private LoginService loginService;

    @Autowired
    private KakaoService kakaoService;

    @Value("${login.token-redirect-url}")
    private String TOKEN_REDIRECT_URL;

    @ResponseBody
    @GetMapping("/oidc/kakao")
    public void kakaoCallback(@RequestParam String code, RedirectAttributes redirectAttributes, HttpServletResponse response) throws IOException {
        TokenResponseDTO token = loginService.KakaoLogin(code);
        setTokenRedirectAttributes(redirectAttributes, token);
        response.sendRedirect(makeTokenRedirectURL(token));
    }

    private static void setTokenRedirectAttributes(RedirectAttributes redirectAttributes, TokenResponseDTO token) {
        redirectAttributes.addAttribute(ACCESS_TOKEN, token.getAccess_token());
        redirectAttributes.addAttribute(REFRESH_TOKEN, token.getRefresh_token());
    }

    private String makeTokenRedirectURL(TokenResponseDTO tokenResponseDto) {
        return TOKEN_REDIRECT_URL + "/login/kakao/?access_token=" + tokenResponseDto.getAccess_token() + "&refresh_token=" + tokenResponseDto.getRefresh_token();
    }

    @Cacheable(cacheNames = "KakaoOIDC", cacheManager = "oidcCacheManager")
    @GetMapping("/oidc/kakao/openkeys")
    public String getOpenKeys() {
        return kakaoService.getOpenKeysFromKakaoOIDC();
    };
}
