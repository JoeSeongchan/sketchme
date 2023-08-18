package com.dutaduta.sketchme.oidc.jwt;

import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
//@Profile({"test"})
@Profile({"test"})
public class JwtUtilImplForLocal implements JwtUtil {
    public UserInfoInAccessTokenDTO extractUserInfo(HttpServletRequest request){
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        long userId = Long.parseLong(token.split(" ")[0]);
        long artistId = Long.parseLong(token.split(" ")[1]);
        return UserInfoInAccessTokenDTO.builder()
                .userId(userId)
                .artistId(artistId).build();
    }
}
