package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

public interface NotifierMessageBroker {

    void sendNotification(String subject, String message, String email, Long userCode);

}
