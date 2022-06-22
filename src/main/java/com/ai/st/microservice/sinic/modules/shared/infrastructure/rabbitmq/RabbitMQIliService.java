package com.ai.st.microservice.sinic.modules.shared.infrastructure.rabbitmq;

import com.ai.st.microservice.common.dto.ili.MicroserviceIli2pgImportSinicDto;
import com.ai.st.microservice.common.dto.ili.MicroserviceIliProcessQueueDto;
import com.ai.st.microservice.sinic.modules.files.domain.FileUUID;
import com.ai.st.microservice.sinic.modules.files.domain.FileUrl;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.IliMessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public final class RabbitMQIliService implements IliMessageBroker {

    private final Logger log = LoggerFactory.getLogger(RabbitMQIliService.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${st.rabbitmq.queueIliSinicLarge.exchange}")
    public String exchangeIliSinicLargeName;

    @Value("${st.rabbitmq.queueIliSinicLarge.routingkey}")
    public String routingkeyIliSinicLargeName;

    @Value("${st.rabbitmq.queueIliSinicSmall.exchange}")
    public String exchangeIliSinicSmallName;

    @Value("${st.rabbitmq.queueIliSinicSmall.routingkey}")
    public String routingkeyIliSinicSmallName;

    @Value("${sinic.database.hostname}")
    private String databaseHost;

    @Value("${sinic.database.port}")
    private String databasePort;

    @Value("${sinic.database.username}")
    private String databaseUsername;

    @Value("${sinic.database.password}")
    private String databasePassword;

    @Value("${sinic.database.database}")
    private String databaseName;

    private static final String MODEL_VERSION = "0.1";
    private static final Long CONCEPT_ID = (long) 4;

    @Override
    public void sendDataToIliProcess(FileUUID uuid, FileUrl url, String schema, int currentFile, int totalFiles,
            boolean isLarge) {

        MicroserviceIli2pgImportSinicDto importData = new MicroserviceIli2pgImportSinicDto();
        importData.setConceptId(CONCEPT_ID);
        importData.setVersionModel(MODEL_VERSION);
        importData.setCurrentFile(currentFile);
        importData.setTotalFiles(totalFiles);
        importData.setDatabaseHost(databaseHost);
        importData.setDatabasePort(databasePort);
        importData.setDatabaseName(databaseName);
        importData.setDatabaseSchema(schema);
        importData.setDatabasePassword(databasePassword);
        importData.setDatabaseUsername(databaseUsername);
        importData.setPathXTF(url.value());
        importData.setReference(uuid.value());

        MicroserviceIliProcessQueueDto data = new MicroserviceIliProcessQueueDto();
        data.setType(MicroserviceIliProcessQueueDto.IMPORT_SINIC);
        data.setImportSinicDto(importData);

        if (isLarge) {
            log.info("File send to large queue");
            rabbitTemplate.convertAndSend(exchangeIliSinicLargeName, routingkeyIliSinicLargeName, data);
        } else {
            log.info("File send to small queue");
            rabbitTemplate.convertAndSend(exchangeIliSinicSmallName, routingkeyIliSinicSmallName, data);
        }

    }

}
