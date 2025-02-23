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
package de.xxx.eventtofile.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.xxx.eventtofile.model.FlightDto;
import org.apache.kafka.clients.admin.AdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final AdminClient adminClient;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, AdminClient adminClient, ObjectMapper objectMapper) {
        this.adminClient = adminClient;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendFlightMsg(FlightDto flightDto) {
        try {
            String msg = this.objectMapper.writeValueAsString(flightDto);
            CompletableFuture<SendResult<String, String>> listenableFuture = this.kafkaTemplate
                    .send(KafkaConfig.FLIGHT_TOPIC, flightDto.id().toString(), msg);
            listenableFuture.get(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Send Flight failed.", e);
        }
        LOGGER.info("send FlightDto msg: {}", flightDto.toString());
    }
}
