package com.dutaduta.sketchme.videoconference.service;


import com.dutaduta.sketchme.global.exception.BadRequestException;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OpenViduAPIService {
    @Value("${openvidu.api.url}")
    private String url;

    @Value("${openvidu.api.secret}")
    private String secret;

    private OpenVidu openVidu;

    @PostConstruct
    public void initOpenVidu(){
        openVidu = new OpenVidu(url,secret);
    }

    public String createSession(String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties sessionProperties = new SessionProperties.Builder().customSessionId(sessionId).build();
        return openVidu.createSession(sessionProperties).getSessionId();
    }

    public void deleteSession(String sessionId) {
        Session session = getSession(sessionId);
        if(session==null){
            throw new BadRequestException("세션이 존재하지 않습니다.");
        }
        try {
            session.close();
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new BadRequestException("세션을 닫을 수 없습니다.");
        }
    }

    public Session getSession(String sessionId) {
        return openVidu.getActiveSession(sessionId);
    }

    public Connection createConnection(String sessionId) {
        Session session = getSession(sessionId);
        if(session==null){
            throw new BadRequestException("세션이 존재하지 않습니다.");
        }
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(OpenViduRole.PUBLISHER)
                .data("user_data")
                .build();
        try {
             return session.createConnection(connectionProperties);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new BadRequestException("연결을 만들 수 없습니다.");
        }
    }


}
