package com.ai.st.microservice.sinic.modules.shared.infrastructure.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${st.rabbitmq.queueResultValidationSinicFiles.queue}")
    public String queueResultValidation;

    @Value("${st.rabbitmq.queueResultValidationSinicFiles.exchange}")
    public String exchangeResultValidation;

    @Value("${st.rabbitmq.queueResultValidationSinicFiles.routingkey}")
    public String routingKeyResultValidation;

    @Bean
    public Queue QueueResultValidation() {
        return new Queue(queueResultValidation, false);
    }

    @Bean
    public DirectExchange exchangeResultValidation() {
        return new DirectExchange(exchangeResultValidation);
    }

    @Bean
    public Binding bindingQueueResultValidation() {
        return BindingBuilder.bind(QueueResultValidation()).to(exchangeResultValidation())
                .with(routingKeyResultValidation);
    }

    // queue ili

    @Value("${st.rabbitmq.queueIli.queue}")
    public String queueIliName;

    @Value("${st.rabbitmq.queueIli.exchange}")
    public String exchangeIliName;

    @Value("${st.rabbitmq.queueIli.routingkey}")
    public String routingkeyIliName;

    @Bean
    public Queue queueIli() {
        return new Queue(queueIliName, false);
    }

    @Bean
    public DirectExchange exchangeIli() {
        return new DirectExchange(exchangeIliName);
    }

    @Bean
    public Binding bindingQueueIli() {
        return BindingBuilder.bind(queueIli()).to(exchangeIli()).with(routingkeyIliName);
    }

    // queue result process sinic files

    @Value("${st.rabbitmq.queueResultProcessSinicFiles.queue}")
    public String queueResultProcessSinicFilesName;

    @Value("${st.rabbitmq.queueResultProcessSinicFiles.exchange}")
    public String queueResultProcessSinicFilesExchange;

    @Value("${st.rabbitmq.queueResultProcessSinicFiles.routingkey}")
    public String queueResultProcessSinicFilesRoutingKey;

    @Bean
    public Queue queueResultProcessSinicFilesName() {
        return new Queue(queueResultProcessSinicFilesName, false);
    }

    @Bean
    public DirectExchange exchangeResultProcessSinicFilesName() {
        return new DirectExchange(queueResultProcessSinicFilesExchange);
    }

    @Bean
    public Binding bindingResultProcessSinicFilesName() {
        return BindingBuilder.bind(queueResultProcessSinicFilesName())
                .to(exchangeResultProcessSinicFilesName())
                .with(queueResultProcessSinicFilesRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
