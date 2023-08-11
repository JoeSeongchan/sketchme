package com.dutaduta.sketchme.chat.config;

import com.dutaduta.sketchme.chat.constant.KafkaConstants;
import com.dutaduta.sketchme.chat.dto.MessageDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class ListenerConfiguration {

    @Value("${kafka.broker}")
    private String kafkaBroker;

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, MessageDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageDTO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MessageDTO> consumerFactory() {

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations());
    }

    //retry 옵션 설정 완료 -> 차후 DLT 설정도 되면 수행
    @Bean
    public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, MessageDTO> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .fixedBackOff(500)
                .maxAttempts(5)
                .concurrency(2)
                .includeTopics(Arrays.asList("chat", "meeting"))
                .create(template);
    }


    @Bean
    public Map<String, Object> consumerConfigurations() {
        Map<String, Object> configurations = new HashMap<>();
        System.out.println(kafkaBroker);
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID_FOR_CHAT);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configurations.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configurations.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
//        configurations.put(JsonDeserializer.TYPE_MAPPINGS,
//                "batch:com.dutaduta.sketchme.reservation, chat:com.dutaduta.sketchme.chat");
        configurations.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configurations.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
//        configurations.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.dutaduta.sketchme");
        //        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configurations.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        configurations.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MessageDTO.class);
        configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return configurations;
    }
}