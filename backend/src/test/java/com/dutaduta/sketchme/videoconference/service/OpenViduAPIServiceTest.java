package com.dutaduta.sketchme.videoconference.service;

import com.dutaduta.sketchme.IntegrationTestSupport;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import io.openvidu.java.client.Connection;
import io.openvidu.java.client.OpenViduException;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OpenViduAPIServiceTest extends IntegrationTestSupport {

    @Autowired
    private OpenViduAPIService openViduAPIService;

    @Test
    @DisplayName("세션을 만든다")
    void createSessionHappyCase() throws OpenViduJavaClientException, OpenViduHttpException {
        // given
        String sessionId = "session-id";

        // when
        String sessionIdFromServer =openViduAPIService.createSession(sessionId);

        // then
        assertThat(sessionIdFromServer).isEqualTo(sessionId);
    }

    @Test
    @DisplayName("세션이 존재하지 않는데 세션을 삭제하려고 하는 경우, 예외가 방출된다.")
    void closeSessionIfSessionIsNotExisted() {
        // given
        String sessionId = "not-existed-session-id";
        // when

        // then
        assertThatThrownBy(()->openViduAPIService.deleteSession(sessionId)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("")
    void createSessionBadCase() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("")
    void createConnectionHappyCase() throws OpenViduJavaClientException, OpenViduHttpException {
        // given
        String sessionId = "session-id";
        String sessionIdFromServer =openViduAPIService.createSession(sessionId);

        // when
        Connection connection = openViduAPIService.createConnection(sessionId);

        // then
        assertThat(connection).isNotNull();
        System.out.println("connection.getToken() = " + connection.getToken());
    }

    @Test
    @DisplayName("존재하지 않는 세션으로 연결을 생성하면 OpenVidu 예외가 방출된다.")
    void createConnectionBadCase() {
        // given
        String sessionId = "not-existed-session-id";

        // when
        assertThatThrownBy(()->openViduAPIService.createConnection(sessionId)).isInstanceOf(BadRequestException.class);

        // then

    }

    @Test
    @DisplayName("세션이 존재하는 경우에 세션을 강제 삭제할 수 있다.")
    public void deleteSessionHappyCase() throws OpenViduJavaClientException, OpenViduHttpException {
     // given
        String sessionId = "session-id";
        String sessionIdFromServer =openViduAPIService.createSession(sessionId);
     // when
        assertDoesNotThrow(()->openViduAPIService.deleteSession(sessionId));

     // then
    }

    @Test
    @DisplayName("세션이 존재하지 않은 경우에 세션을 강제 삭제할 수 없다.")
    public void deleteSessionBadCase(){
     // given

     // when

     // then
    }


}