package com.dutaduta.sketchme.chat.config;

import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.TokenExpiredException;
import com.dutaduta.sketchme.global.exception.UnauthorizedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Log4j2
@Component
public class StompExceptionHandler extends StompSubProtocolErrorHandler {

    public StompExceptionHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        final Throwable exception = ex;

        if (exception instanceof TokenExpiredException) {
            return errorMessage("토큰 유효 기한이 지났습니다.");
        }

        if (exception.getCause() instanceof UnauthorizedException) {
            return errorMessage("토큰이 존재하지 않습니다.");
        }

        if(exception instanceof RuntimeException) {
            return errorMessage("잘못된 요청입니다. 정확한 요청을 해주세요");
        }

        return errorMessage("서버 내부에서 오류가 발생하였습니다");
    }


    private Message<byte[]> errorMessage(String errorMessage) {
        log.info("errorMessage");
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8),
                accessor.getMessageHeaders());
    }

    @Override
    public Message<byte[]> handleErrorMessageToClient(Message<byte[]> errorMessage) {
        log.info("handleErrorMessageToClient");
        return super.handleErrorMessageToClient(errorMessage);
    }

    @Override
    protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor, byte[] errorPayload, Throwable cause, StompHeaderAccessor clientHeaderAccessor) {
        log.info("handleInternal");
        return super.handleInternal(errorHeaderAccessor, errorPayload, cause, clientHeaderAccessor);
    }
}
