package com.dutaduta.sketchme.reservation.config;

import com.dutaduta.sketchme.reservation.domain.MemberType;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@StepScope
@Component
public class DateTimeCreator {

    @Value("#{jobParameters[memberType]}")
    String memberType;

    public LocalDateTime dateTimeBuilder() {
        LocalDateTime current = LocalDateTime.now();
        int time = createTime(current.getMinute());

        return LocalDateTime.of(current.getYear(),
                current.getMonth(),
                current.getDayOfMonth(),
                current.getHour(),
                time);
    }


    //10분전, 정각인 상황을 처리, 아직 초기본. 잘못된 점이 있을 수 있음 -> 피드백 필요
    private int createTime(int time) {
        if (MemberType.BOT_LIVE_STARTED.name().equals(memberType)) {
            if (time >= 50 && time < 0) {
                return 0;
            }
            return 30;
        }

        //if(MemberType.BOT_LIVE_INFO.name().equals(memberType)) {
        if (time < 30) {
            return 0;
        }
        return 30;
        //}
    }
}
