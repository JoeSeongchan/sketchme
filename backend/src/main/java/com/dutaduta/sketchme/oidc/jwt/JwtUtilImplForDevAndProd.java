package com.dutaduta.sketchme.oidc.jwt;

import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
//@Profile({"local","prod","dev"})
@Profile({"local","prod","dev"})
public class JwtUtilImplForDevAndProd implements JwtUtil {
    public UserInfoInAccessTokenDTO extractUserInfo(HttpServletRequest request){
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        Long userId = JwtProvider.getUserId(token, secretKey);
        Long artistId = JwtProvider.getArtistId(token, secretKey);
        return UserInfoInAccessTokenDTO.builder()
                .userId(userId)
                .artistId(artistId).build();
    }
}
