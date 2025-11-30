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
package de.xxx.sourcesink.adapter.event

import de.xxx.sourcesink.domain.model.FlightDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper
import java.util.concurrent.TimeUnit

@Component
class KafkaProducer(val kafkaTemplate: KafkaTemplate<String, String>,
                    val objectMapper: JsonMapper) {
    private val LOGGER: Logger = LoggerFactory.getLogger(KafkaProducer::class.java)

    fun sendFlightMsg(flightDto: FlightDto) {
        try {
            val msg = objectMapper.writeValueAsString(flightDto)
            val listenableFuture = kafkaTemplate
                .send(KafkaConfig.FLIGHT_TOPIC, flightDto.id.toString(), msg)
            listenableFuture.get(15, TimeUnit.SECONDS)
        } catch (e: Exception) {
            throw RuntimeException("Send Flight failed.", e)
        }
        LOGGER.info("send FlightDto msg: {}", flightDto.toString())
    }
}