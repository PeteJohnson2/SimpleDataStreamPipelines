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
package de.xxx.eventtofile.source;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.xxx.eventtofile.kafka.KafkaProducer;
import de.xxx.eventtofile.model.FlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FlightSourceService {
    private static final Logger log = LoggerFactory.getLogger(FlightSourceService.class);
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    public FlightSourceService(KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public void handleFlightSourceEvent(FlightDto flightDto) {
        this.logDto(flightDto);
        this.kafkaProducer.sendCountryMsg(flightDto);
    }

    private void logDto(FlightDto flightDto) {
        try {
            log.info(this.objectMapper.writeValueAsString(flightDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
