package com.dutaduta.sketchme.product.service;

import com.dutaduta.sketchme.IntegrationTestSupport;
import com.dutaduta.sketchme.SketchmeApplication;
import com.dutaduta.sketchme.common.Constant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TimelapseServiceTest extends IntegrationTestSupport {
    @Autowired
    private TimelapseService timelapseService;

    @Configuration
    @Import(SketchmeApplication.class)
    static class ContextConfiguration {
        @Bean
        @Primary
        public Executor executor() {
            return new SyncTaskExecutor();
        }
    }

    @Test
    @DisplayName("타임랩스를 만든다. (Happy Case)")
    void makeTimelapseHappyCase() throws ExecutionException, InterruptedException {
        // given
        // 타임랩스의 기반이 되는 라이브 파일들을 미리 준비한다.
        // 코드로 라이브 파일을 준비하지 않고, meetingId가 Long.MAX_VALUE일 때의 경로에 미리 테스트용 파일을 넣어놓는다.
        long meetingId = Long.MAX_VALUE;
        System.out.println("meetingId = " + meetingId);
        // when
        CompletableFuture<String> timelapsePath = timelapseService.makeTimelapse(meetingId);
        // then

        assertThat(timelapsePath.get()).isEqualTo(Constant.TIMELAPSE_DIRECTORY+"/9223372036854775807/timelapse.gif");
    }

    @Test
    @DisplayName("라이브 그림이 없는데 타임랩스를 만들면 예외가 발생한다.")
    void makeTimelapseWithNoLivePicture() throws ExecutionException, InterruptedException {
        // given
        // 타임랩스의 기반이 되는 라이브 파일들을 미리 준비한다.
        // 코드로 라이브 파일을 준비하지 않고, meetingId가 Long.MAX_VALUE일 때의 경로에 미리 테스트용 파일을 넣어놓는다.
        long meetingId = Long.MAX_VALUE-1;
        // when
        CompletableFuture<String> timelapsePath = timelapseService.makeTimelapse(meetingId);
        // then

        assertThat(timelapsePath).isNotCompleted();
    }

    @Test
    @DisplayName("타임랩스 썸네일을 만든다. (Happy Case)")
    void makeTimelapseThumbnailHappyCase() throws ExecutionException, InterruptedException {
        // given
        // 타임랩스를 만든다.
        long meetingId = Long.MAX_VALUE;
        System.out.println("meetingId = " + meetingId);
        // when
        CompletableFuture<String> timelapsePathFuture = timelapseService.makeTimelapse(meetingId);
        // then

        String timelapsePath = timelapsePathFuture.get();

        // when
        String thumbnailPath = timelapseService.makeTimelapseThumbnail(meetingId,timelapsePath).get();
        // then
        assertThat(thumbnailPath).isEqualTo(Constant.TIMELAPSE_DIRECTORY+"/9223372036854775807/timelapse-thumbnail.png");
    }
}