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

import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.DefaultHostResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableKafka
@EnableTransactionManagement
class KafkaConfig(val producerFactory: ProducerFactory<String, String>) {
    companion object {
        @JvmStatic
        val FLIGHT_TOPIC: String = "flight-source-topic"
    }
    private val log: Logger = LoggerFactory.getLogger(KafkaConfig::class.java)


    @Value("\${kafka.server.name}")
    private lateinit var kafkaServerName: String

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${spring.kafka.producer.compression-type}")
    private lateinit var compressionType: String

    @PostConstruct
    fun init() {
        val bootstrap =
            bootstrapServers!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
        if (bootstrap.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$".toRegex())) {
            DefaultHostResolver.IP_ADDRESS = bootstrap
        } else if (!bootstrap.isEmpty()) {
            DefaultHostResolver.KAFKA_SERVICE_NAME = bootstrap
        }
        log.info(
            "Kafka Servername: ${DefaultHostResolver.KAFKA_SERVER_NAME} Kafka Servicename: ${DefaultHostResolver.KAFKA_SERVICE_NAME} Ip Address: ${DefaultHostResolver.IP_ADDRESS}")
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        val kafkaTemplate = KafkaTemplate(this.producerFactory)
        return KafkaTemplate(this.producerFactory)
    }
}