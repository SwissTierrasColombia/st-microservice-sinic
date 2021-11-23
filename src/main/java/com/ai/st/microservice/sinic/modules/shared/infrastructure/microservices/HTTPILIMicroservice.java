package com.ai.st.microservice.sinic.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.IliFeignClient;
import com.ai.st.microservice.common.dto.ili.MicroserviceIlivalidatorBackgroundDto;
import com.ai.st.microservice.sinic.modules.files.domain.FileUUID;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.ILIMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.MicroserviceUnreachable;
import org.springframework.stereotype.Service;

@Service
public final class HTTPILIMicroservice implements ILIMicroservice {

    private static final String MODEL_VERSION = "1.1";
    private static final Long CONCEPT_ID = (long) 3;
    private static final String QUEUE_UPDATE_STATE_XTF_SINIC_FILES = "QUEUE_UPDATE_STATE_XTF_SINIC_FILES";

    private final IliFeignClient iliFeignClient;

    public HTTPILIMicroservice(IliFeignClient iliFeignClient) {
        this.iliFeignClient = iliFeignClient;
    }

    @Override
    public void sendToValidation(FileUUID fileUUID, String pathFile, boolean skipGeometryValidation, boolean skipErrors) {

        try {

            MicroserviceIlivalidatorBackgroundDto request = new MicroserviceIlivalidatorBackgroundDto();
            request.setConceptId(CONCEPT_ID);
            request.setVersionModel(MODEL_VERSION);
            request.setQueueResponse(QUEUE_UPDATE_STATE_XTF_SINIC_FILES);
            request.setPathFile(pathFile);
            request.setSkipErrors(skipErrors);
            request.setReferenceId(fileUUID.value());
            request.setSkipGeometryValidation(skipGeometryValidation);

            iliFeignClient.startValidation(request);

        } catch (Exception e) {
            throw new MicroserviceUnreachable("ili");
        }

    }

}
