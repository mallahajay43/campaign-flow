package me.mallahajay43.campaignflow.common.config;

import me.mallahajay43.campaignflow.common.exceptions.ImportFileNotFoundException;
import me.mallahajay43.campaignflow.common.exceptions.InvalidCsvStructureException;
import me.mallahajay43.campaignflow.common.exceptions.InvalidImportEventException;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, exception) ->
                                new TopicPartition(record.topic() + ".DLT", record.partition())
                );

        FixedBackOff retryPolicy = new FixedBackOff(50_000L, 5L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, retryPolicy);

        /*
         * Generic non-retryable exceptions.
         */
        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,
                InvalidImportEventException.class,
                ImportFileNotFoundException.class,
                InvalidCsvStructureException.class
        );

        return errorHandler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerFactory, DefaultErrorHandler kafkaErrorHandler) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();

        factory.setConsumerFactory(consumerFactory);

        factory.setCommonErrorHandler(kafkaErrorHandler);

        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }
}