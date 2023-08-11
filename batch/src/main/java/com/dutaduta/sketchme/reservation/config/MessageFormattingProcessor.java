package com.dutaduta.sketchme.reservation.config;

import com.dutaduta.sketchme.reservation.domain.Meeting;
import com.dutaduta.sketchme.reservation.domain.MessageCreator;
import com.dutaduta.sketchme.reservation.dto.MessageDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@StepScope
@Component
public class MessageFormattingProcessor implements ItemProcessor<Meeting, MessageDTO> {

    @Value("#{jobParameters[memberType]}")
    private String memberType;

    @Override
    public MessageDTO process(Meeting meeting) {
        String content = MessageCreator.createContent(meeting, memberType);
        MessageDTO messageDTO = MessageDTO.of(meeting, content, memberType);
        return messageDTO;
    }
}
