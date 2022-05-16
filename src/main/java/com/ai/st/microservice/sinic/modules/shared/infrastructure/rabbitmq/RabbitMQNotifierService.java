package com.ai.st.microservice.sinic.modules.shared.infrastructure.rabbitmq;

import com.ai.st.microservice.common.dto.notifier.MicroserviceNotificationDto;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.NotifierMessageBroker;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQNotifierService implements NotifierMessageBroker {

    @Value("${st.rabbitmq.queueNotifications.exchange}")
    public String exchangeFilesName;

    @Value("${st.rabbitmq.queueNotifications.routingkey}")
    public String routingkeyFilesName;

    private final AmqpTemplate rabbitTemplate;

    public RabbitMQNotifierService(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendNotification(String subject, String message, String email, Long userCode) {

        MicroserviceNotificationDto data = new MicroserviceNotificationDto();
        data.setEmail(email);
        data.setSubject(subject);
        data.setMessage(message);
        data.setUserCode(userCode);
        data.setType("email");
        data.setStatus(1);

        rabbitTemplate.convertAndSend(exchangeFilesName, routingkeyFilesName, data);
    }

}
