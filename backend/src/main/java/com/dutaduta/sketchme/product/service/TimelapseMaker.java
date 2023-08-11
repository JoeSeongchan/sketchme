package com.dutaduta.sketchme.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TimelapseMaker {
    // TODO : 구현
    public void makeTimelapse(long meetingId){
        // meeting ID를 가지고서 라이브 사진 경로에 있는 1~{마지막 사진 번호}.png를 가져온다.

        // gif 툴을 가지고서 타임랩스를 만든다.

        // gif를 만들고 이를 {타임랩스 경로}에 저장한다.

        // 저장하고 나서 Message Broker 에 다 만들었다는 사실을 발행한다.
            // Stomp 이용

        // Message Broker 를 구독하고 있는 애가 그 메시지를 받아서 웹 소켓으로 클라이언트에게 타임랩스 gif를 전송한다.

        // 끝
    }
}
