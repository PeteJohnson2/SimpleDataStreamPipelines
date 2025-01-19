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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.ProducerFactory

@Configuration
@EnableKafka
class KafkaConfig {
    companion object {
        @JvmStatic
        val FLIGHT_TOPIC: String = "flight-topic"
        @JvmStatic
        val DEFAULT_DLT_TOPIC: String = "flight-topic-dlt"
    }
    private val log: Logger = LoggerFactory.getLogger(KafkaConfig::class.java)

    private val producerFactory: ProducerFactory<String, String>? = null

    @Value("\${kafka.server.name}")
    private val kafkaServerName: String? = null

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${spring.kafka.producer.transaction-id-prefix}")
    private val transactionIdPrefix: String? = null

    @Value("\${spring.kafka.producer.compression-type}")
    private val compressionType: String? = null
}