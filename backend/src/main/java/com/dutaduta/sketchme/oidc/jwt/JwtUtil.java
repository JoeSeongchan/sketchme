package com.dutaduta.sketchme.oidc.jwt;

import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtUtil {
    UserInfoInAccessTokenDTO extractUserInfo(HttpServletRequest request);
}
