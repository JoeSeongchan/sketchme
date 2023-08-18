package com.dutaduta.sketchme.reservation.config;

import com.dutaduta.sketchme.reservation.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@AllArgsConstructor
public class MessageScheduler {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager pm;
    private final SendInfoMessageBelongToMeeting beforeTenMinute;

    @Scheduled(cron = "0 0/1 * * * *")
    public void perform() throws Exception {
        JobParameters param = new JobParametersBuilder()
                .addString("sendMessageBeforeTenMinuteJob", String.valueOf(System.currentTimeMillis()))
                .addString("memberType", MemberType.BOT_LIVE_INFO.name())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(
                beforeTenMinute.sendInfoMessageJob(
                        jobRepository, beforeTenMinute.sendInfoMessageStep(jobRepository, pm)),param);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendMessageWhenMeetingStarted() throws Exception {
        JobParameters param = new JobParametersBuilder()
                .addString("sendMessageWhenMeetingStarted", String.valueOf(System.currentTimeMillis()))
                .addString("memberType", MemberType.BOT_LIVE_STARTED.name())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(
                beforeTenMinute.sendInfoMessageJob(
                        jobRepository, beforeTenMinute.sendInfoMessageStep(jobRepository, pm)),param);
    }
}
