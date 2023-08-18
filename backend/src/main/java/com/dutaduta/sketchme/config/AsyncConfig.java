package com.dutaduta.sketchme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Async 어노테이션은 쓰레드를 재사용하지 않는다. 매번 새로운 스레드를 만들기 때문에
 * 필수적으로 쓰레드 풀을 사용해야 한다.
 * 디폴트 타임아웃 설정이 안 되어 있기 때문에 잘 종료되는지 체크해야 한다.
 */
@Configuration // 스프링 설정 추가
@Profile("!test") // Profile이 'test'가 아닐 때만 Async 적용
public class AsyncConfig { // 비동기 설정

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 코어 스레드 풀 크기 설정
        executor.setMaxPoolSize(20); // 최대 스레드 풀 크기 설정
        executor.setQueueCapacity(500); // 작업 큐 용량 설정
        executor.setThreadNamePrefix("AsyncThread-"); // 스레드 이름 접두사 설정

        // 작업이 완료된 후 스레드 풀이 종료될 때까지 대기할 시간 설정 (단위: 초)
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }

}
