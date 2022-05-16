package com.ai.st.microservice.sinic.modules.shared.infrastructure.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQNotificationsConfiguration {

    @Value("${st.rabbitmq.queueNotifications.queue}")
    public String queueNotificationName;

    @Value("${st.rabbitmq.queueNotifications.exchange}")
    public String exchangeNotificationName;

    @Value("${st.rabbitmq.queueNotifications.routingkey}")
    public String routingkeyNotificationName;

    @Bean
    public Queue queue() {
        return new Queue(queueNotificationName, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeNotificationName);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkeyNotificationName);
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
