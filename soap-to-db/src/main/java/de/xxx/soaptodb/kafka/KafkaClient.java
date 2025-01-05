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
package de.xxx.soaptodb.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.xxx.soaptodb.model.CountryDto;
import org.apache.kafka.clients.admin.AdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaClient.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AdminClient adminClient;

    public KafkaClient(KafkaTemplate<String, String> kafkaTemplate,AdminClient adminClient) {
        this.adminClient = adminClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCountryMsg(CountryDto countryDto) {
        try {
            String msg = this.objectMapper.writeValueAsString(countryDto);
            CompletableFuture<SendResult<String, String>> listenableFuture = this.kafkaTemplate
                    .send(KafkaConfig.COUNTRY_TOPIC, countryDto.getId().toString(), msg);
            listenableFuture.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Send Country failed.", e);
        }
        LOGGER.info("send CountryDto msg: {}", countryDto.toString());
    }
}
