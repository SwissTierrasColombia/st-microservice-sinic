package com.ai.st.microservice.sinic.entrypoints.events.files;

import com.ai.st.microservice.common.dto.ili.MicroserviceResultSinicImportFile;
import com.ai.st.microservice.sinic.modules.files.application.update_file_status.FileStatusUpdater;
import com.ai.st.microservice.sinic.modules.files.application.update_file_status.FileStatusUpdaterCommand;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.SCMTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public final class UpdateStatusXTFOnSinicStatusImportChanged {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final FileStatusUpdater fileStatusUpdater;

    public UpdateStatusXTFOnSinicStatusImportChanged(FileStatusUpdater fileStatusUpdater) {
        this.fileStatusUpdater = fileStatusUpdater;
    }

    @RabbitListener(queues = "${st.rabbitmq.queueResultProcessSinicFiles.queue}", concurrency = "${st.rabbitmq.queueResultProcessSinicFiles.concurrency}")
    public void resultValidation(MicroserviceResultSinicImportFile resultImportData) {

        try {

            log.info("Updating state xtf from sinic import: " + resultImportData.getResult().name());

            String fileId = resultImportData.getReference();

            log.info("reference uuid: " + resultImportData.getReference());

            if (fileId != null && !fileId.isEmpty()) {

                FileStatusUpdaterCommand.Status status = mappingEnum(resultImportData.getResult());

                fileStatusUpdater.handle(new FileStatusUpdaterCommand(status, fileId, null, null));
            }

        } catch (Exception e) {
            String messageError = String.format(
                    "Error actualizando el estado del archivo %s durante la importación: %s",
                    resultImportData.getReference(), e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
        }

    }

    private FileStatusUpdaterCommand.Status mappingEnum(MicroserviceResultSinicImportFile.Status status) {
        switch (status) {
        case IMPORTING:
            return FileStatusUpdaterCommand.Status.IMPORTING;
        case SUCCESS_IMPORT:
            return FileStatusUpdaterCommand.Status.IMPORT_SUCCESSFUL;
        case FAILED_IMPORT:
            return FileStatusUpdaterCommand.Status.IMPORT_UNSUCCESSFUL;
        default:
            throw new RuntimeException("Error mapping enum");
        }
    }

}
