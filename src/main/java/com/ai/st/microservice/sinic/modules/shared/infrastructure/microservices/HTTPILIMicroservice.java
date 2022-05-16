package com.ai.st.microservice.sinic.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.IliFeignClient;
import com.ai.st.microservice.common.dto.ili.MicroserviceIlivalidatorBackgroundDto;
import com.ai.st.microservice.sinic.modules.files.domain.FileUUID;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.ILIMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.MicroserviceUnreachable;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.SCMTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class HTTPILIMicroservice implements ILIMicroservice {

    private final Logger log = LoggerFactory.getLogger(HTTPILIMicroservice.class);

    private static final String MODEL_VERSION = "0.1";
    private static final Long CONCEPT_ID = (long) 4;
    private static final String QUEUE_UPDATE_STATE_XTF_SINIC_FILES = "QUEUE_UPDATE_STATE_XTF_SINIC_FILES";

    private final IliFeignClient iliFeignClient;

    public HTTPILIMicroservice(IliFeignClient iliFeignClient) {
        this.iliFeignClient = iliFeignClient;
    }

    @Override
    public void sendToValidation(FileUUID fileUUID, UserCode userCode, String pathFile, boolean skipGeometryValidation,
            boolean skipErrors) {

        try {

            MicroserviceIlivalidatorBackgroundDto request = new MicroserviceIlivalidatorBackgroundDto();
            request.setConceptId(CONCEPT_ID);
            request.setVersionModel(MODEL_VERSION);
            request.setQueueResponse(QUEUE_UPDATE_STATE_XTF_SINIC_FILES);
            request.setPathFile(pathFile);
            request.setSkipErrors(skipErrors);
            request.setReferenceId(fileUUID.value());
            request.setSkipGeometryValidation(skipGeometryValidation);
            request.setUserCode(userCode.value());

            iliFeignClient.startValidation(request);

        } catch (Exception e) {
            String messageError = String.format("Error enviando el archivo XTF %s a validación: %s", fileUUID.value(),
                    e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new MicroserviceUnreachable("ili");
        }

    }

}
