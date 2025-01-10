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

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.DefaultHostResolver;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    private Logger log = LoggerFactory.getLogger(KafkaConfig.class);
    public static final String COUNTRY_TOPIC = "country-topic";
    public static final String DEFAULT_DLT_TOPIC = "country-topic-dlt";

    private final ProducerFactory<String, String> producerFactory;
    @Value("${kafka.server.name}")
    private String kafkaServerName;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.transaction-id-prefix}")
    private String transactionIdPrefix;
    @Value("${spring.kafka.producer.compression-type}")
    private String compressionType;

    public KafkaConfig(ProducerFactory<String, String> producerFactory) {
        this.producerFactory = producerFactory;
    }

    @PostConstruct
    public void init() {
        String bootstrap = this.bootstrapServers.split(":")[0].trim();
        if (bootstrap.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
            DefaultHostResolver.IP_ADDRESS = bootstrap;
        } else if(!bootstrap.isEmpty()) {
            DefaultHostResolver.KAFKA_SERVICE_NAME = bootstrap;
        }
        log.info("Kafka Servername: {} Kafka Servicename: {} Ip Address: {}", DefaultHostResolver.KAFKA_SERVER_NAME,
                DefaultHostResolver.KAFKA_SERVICE_NAME, DefaultHostResolver.IP_ADDRESS);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(this.producerFactory);
        kafkaTemplate.setTransactionIdPrefix(this.transactionIdPrefix);
        return new KafkaTemplate<>(this.producerFactory);
    }

    @Bean("kafkaRetryTemplate")
    public KafkaTemplate<String, String> kafkaRetryTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(this.producerFactory);
        kafkaTemplate.setTransactionIdPrefix(this.transactionIdPrefix);
        kafkaTemplate.setAllowNonTransactional(true);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTransactionManager<String, String> kafkaTransactionManager() {
        KafkaTransactionManager<String, String> manager = new KafkaTransactionManager<>(this.producerFactory);
        return manager;
    }

    @Bean
    public NewTopic newUserTopic() {
        return TopicBuilder.name(KafkaConfig.COUNTRY_TOPIC)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, this.compressionType).compact().build();
    }

    @Bean
    public DeadLetterPublishingRecoverer recoverer(
            @Qualifier("kafkaRetryTemplate") KafkaTemplate<?, ?> bytesKafkaTemplate,
            @Qualifier("kafkaRetryTemplate") KafkaTemplate<?, ?> kafkaTemplate) {
        Map<Class<?>, KafkaOperations<? extends Object, ? extends Object>> templates = new LinkedHashMap<>();
        templates.put(byte[].class, bytesKafkaTemplate);
        templates.put(String.class, kafkaTemplate);
        return new DeadLetterPublishingRecoverer(templates);
    }

    @Bean
    public AdminClient kafkaAdminClient() {
        return KafkaAdminClient.create(this.producerFactory.getConfigurationProperties());
    }

    @Bean
    public NewTopic defaultDltTopic() {
        return TopicBuilder.name(KafkaConfig.DEFAULT_DLT_TOPIC)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, this.compressionType).compact().build();
    }
}
