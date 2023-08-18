package com.dutaduta.sketchme.reservation.config;


import com.dutaduta.sketchme.reservation.constant.KafkaConstants;
import com.dutaduta.sketchme.reservation.domain.Meeting;
import com.dutaduta.sketchme.reservation.dto.MessageDTO;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Configuration
public class SendInfoMessageBelongToMeeting {

    private final EntityManagerFactory emf;
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    private final MessageFormattingProcessor messageFormattingProcessor;
    private final DateTimeCreator dateTimeCreator;

    public SendInfoMessageBelongToMeeting(EntityManagerFactory emf,
                                          KafkaTemplate<String, MessageDTO> kafkaTemplate,
                                          MessageFormattingProcessor messageFormattingProcessor,
                                          DateTimeCreator dateTimeCreator) {
        this.emf = emf;
        this.kafkaTemplate = kafkaTemplate;
        this.messageFormattingProcessor = messageFormattingProcessor;
        this.dateTimeCreator = dateTimeCreator;
        kafkaTemplate.setDefaultTopic(KafkaConstants.KAFKA_TOPIC);
    }

    @Bean
    public Job sendInfoMessageJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("sendMessageBeforeTenMinuteJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    @JobScope
    public Step sendInfoMessageStep(JobRepository jobRepository, PlatformTransactionManager pm) {
        return new StepBuilder("stepBeforeTenMinute", jobRepository)
                .<Meeting, MessageDTO>chunk(1000, pm)
                .reader(reader())
                .processor(messageFormattingProcessor)
                .writer(kafkaItemWriter(kafkaTemplate))
                .build();
    }


    //여기 현재 테스트 필요. 거의 완성
    @Bean
    @StepScope
    public JpaPagingItemReader<Meeting> reader() {
        LocalDateTime targetDateTime = dateTimeCreator.dateTimeBuilder();
        return new JpaPagingItemReaderBuilder<Meeting>() //list
                .name("readerBeforeTenMinute")
                .entityManagerFactory(emf)
                .pageSize(1000)
                .queryString(
                        "SELECT m FROM Meeting m WHERE m.startDateTime = :targetDateTime AND " +
                                "meetingStatus = APPROVED") //여기 바꿔야 함.
                .parameterValues(Collections.<String, Object>singletonMap("targetDateTime", targetDateTime))
                .build();
    }

    @Bean
    @StepScope
    public KafkaItemWriter<String, MessageDTO> kafkaItemWriter(KafkaTemplate<String, MessageDTO> kafkaTemplate) {
        return new KafkaItemWriterBuilder<String, MessageDTO>()
                .kafkaTemplate(kafkaTemplate)
                .itemKeyMapper(source -> {
                    log.info(source.toString());
                    return source.getSenderID().toString();
                })
                .build();
    }
}
