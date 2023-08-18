package com.dutaduta.sketchme.chat.config;

import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.TokenExpiredException;
import com.dutaduta.sketchme.global.exception.UnauthorizedException;
import com.dutaduta.sketchme.oidc.domain.TokenObject;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        checkAuthorizationWhenSubscribe(stompHeaderAccessor);
        return ChannelInterceptor.super.preSend(message, channel);
    }

    private void checkAuthorizationWhenSubscribe(StompHeaderAccessor stompHeaderAccessor) {
        if (!StompCommand.SUBSCRIBE.equals(stompHeaderAccessor.getCommand())) return;
        log.info("SUBSCRIBE");

        List<String> tokenHeader = stompHeaderAccessor.getNativeHeader("Authorization");
        String jwtToken = tokenHeader.get(0);
        String secretKey = JwtProvider.getSecretKey();
        TokenObject tokenObject = new TokenObject(jwtToken);

        validate(secretKey, tokenObject);

        // Token에서 UserId 꺼내기
        Long userId = JwtProvider.getUserId(tokenObject.getToken(), secretKey);
        //1번 문제
        String[] destinationBeforeParsed = stompHeaderAccessor.getDestination().split("/");
        Long subscriptionId = Long.parseLong(destinationBeforeParsed[destinationBeforeParsed.length - 1]);

        if (!userId.equals(subscriptionId)) throw new BadRequestException("잘못된 요청입니다. 정확한 요청을 해주세요");
    }

    private void validate(String secretKey, TokenObject tokenObject) {
        if (tokenObject.getToken().equals("null")) { //이거 도대체 왜 null이 스트링이냐
            throw new UnauthorizedException("토큰이 존재하지 않습니다.");
        }

        if (!tokenObject.validateToken(secretKey)) {
            throw new TokenExpiredException("토큰 유효 기한이 지났습니다.");
        }

        if (JwtProvider.isAccessToken(tokenObject.getToken(), secretKey) &&
                redisTemplate.opsForValue().get(tokenObject.getToken()) != null) {
            throw new UnauthorizedException("이미 로그아웃된 토큰입니다.");
        }
    }
}
