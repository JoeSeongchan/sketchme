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
        current = createTime(current);
        return LocalDateTime.of(current.getYear(),
                current.getMonth(),
                current.getDayOfMonth(),
                current.getHour(),
                current.getMinute());
    }


    private LocalDateTime createTime(LocalDateTime localDateTime) {
        int time = localDateTime.getMinute();

        if (MemberType.BOT_LIVE_STARTED.name().equals(memberType)) {
            if (time >= 0 && time <= 10) {
                return localDateTime.minusMinutes(time);
            }
            if (time >= 30 && time <= 40) {
                return localDateTime.minusMinutes(time).plusMinutes(30);
            }
        } else if (MemberType.BOT_LIVE_INFO.name().equals(memberType)) {
            //30분 전부터 보내도록 수정
            if (time >= 30) {
                return localDateTime.minusMinutes(time).plusMinutes(60);
            }
            if (time <= 30) {
                return localDateTime.minusMinutes(time).plusMinutes(30);
            }
        }
        return localDateTime;
    }
}
