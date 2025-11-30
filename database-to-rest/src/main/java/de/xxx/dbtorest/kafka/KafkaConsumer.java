/**
 *    Copyright 2023 Sven Loesekann
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package de.xxx.dbtorest.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.xxx.dbtorest.model.DbChangeDto;
import de.xxx.dbtorest.model.KafkaEventDto;
import de.xxx.dbtorest.sink.DbChangeSinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Transactional
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ObjectMapper objectMapper;
    private final DbChangeSinkService dbChangeSinkService;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaConsumer(ObjectMapper objectMapper,
                         DbChangeSinkService dbChangeSinkService,
                         KafkaTemplate<String,String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.dbChangeSinkService = dbChangeSinkService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @RetryableTopic(kafkaTemplate = "kafkaRetryTemplate", attempts = "3", backOff = @BackOff(delay = 1000, multiplier = 2.0),
            autoCreateTopics = "true", topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = KafkaConfig.ORDERPRODUCT_TOPIC)
    public void consumerForCountryTopic(String message) {
        //LOGGER.info("consumerForCountryTopic [{}]", message);
        try {
            DbChangeDto dto = this.objectMapper.readValue(message, DbChangeDto.class);
            this.dbChangeSinkService.handleDbChange(dto);
        } catch (Exception e) {
            LOGGER.warn("send failed orderproduct-topic [{}]", message);
            this.sendToDefaultDlt(new KafkaEventDto(KafkaConfig.DEFAULT_DLT_TOPIC, message));
        }
    }

    @DltHandler
    public void consumerForDlt(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        LOGGER.info(in + " from " + topic);
    }

    private boolean sendToDefaultDlt(KafkaEventDto dto) {
        try {
            CompletableFuture<SendResult<String, String>> listenableFuture = this.kafkaTemplate
                    .send(KafkaConfig.DEFAULT_DLT_TOPIC, UUID.randomUUID().toString(), this.objectMapper.writeValueAsString(dto));
            listenableFuture.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Message send to {}. {}", KafkaConfig.DEFAULT_DLT_TOPIC, dto.toString());
        return true;
    }

}
