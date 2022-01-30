package com.ai.st.microservice.sinic.entrypoints.events.files;

import com.ai.st.microservice.common.dto.ili.MicroserviceValidationDto;
import com.ai.st.microservice.sinic.modules.files.application.update_file_status.FileStatusUpdater;
import com.ai.st.microservice.sinic.modules.files.application.update_file_status.FileStatusUpdaterCommand;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.CompressorFile;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public final class UpdateStateXTFOnValidationDone {

    @Value("${st.filesDirectory}")
    private String stFilesDirectory;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final FileStatusUpdater fileStatusUpdater;
    private final CompressorFile compressorFile;

    public UpdateStateXTFOnValidationDone(FileStatusUpdater fileStatusUpdater, CompressorFile compressorFile) {
        this.fileStatusUpdater = fileStatusUpdater;
        this.compressorFile = compressorFile;
    }

    @RabbitListener(queues = "${st.rabbitmq.queueResultValidationSinicFiles.queue}", concurrency = "${st.rabbitmq.queueResultValidationSinicFiles.concurrency}")
    public void resultValidation(MicroserviceValidationDto validationDto) {

        try {

            log.info("Updating state xtf with result: " + validationDto.getIsValid());

            String logPath = null;
            try {
                if (validationDto.getLog() != null) {
                    String zipName = RandomStringUtils.random(10, true, false);
                    logPath = compressorFile.compress(new File(validationDto.getLog()), buildNamespace(), zipName);
                }
            } catch (Exception e) {
                log.error("Error comprimiendo el log: " + e.getMessage());
            }

            String referenceId = validationDto.getReferenceId();

            if (referenceId != null && !referenceId.isEmpty()) {

                FileStatusUpdaterCommand.Status status = (validationDto.getIsValid())
                        ? FileStatusUpdaterCommand.Status.SUCCESSFUL : FileStatusUpdaterCommand.Status.UNSUCCESSFUL;

                fileStatusUpdater.handle(new FileStatusUpdaterCommand(status, validationDto.getReferenceId(), logPath, validationDto.getUserCode()));
            }

        } catch (Exception e) {
            log.error("Error actualizando el estado del archivo xtf: " + e.getMessage());
        }

    }

    private String buildNamespace() {
        return stFilesDirectory + "/sinic/entregas/logs";
    }

}
