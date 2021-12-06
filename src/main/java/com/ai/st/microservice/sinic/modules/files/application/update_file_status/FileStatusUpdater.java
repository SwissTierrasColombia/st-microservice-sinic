package com.ai.st.microservice.sinic.modules.files.application.update_file_status;

import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.files.domain.*;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

import java.util.List;

@Service
public final class FileStatusUpdater implements CommandUseCase<FileStatusUpdaterCommand> {

    private final DeliveryRepository deliveryRepository;
    private final FileRepository fileRepository;

    public FileStatusUpdater(DeliveryRepository deliveryRepository, FileRepository fileRepository) {
        this.deliveryRepository = deliveryRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public void handle(FileStatusUpdaterCommand command) {

        FileUUID uuid = new FileUUID(command.fileUUID());

        FileStatus status = null;
        FileLog log = null;

        if (command.status().equals(FileStatusUpdaterCommand.Status.SUCCESSFUL)) {
            status = new FileStatus(FileStatus.Status.SUCCESSFUL);
        }

        if (command.status().equals(FileStatusUpdaterCommand.Status.UNSUCCESSFUL)) {
            log = FileLog.fromValue(command.log());
            status = new FileStatus(FileStatus.Status.UNSUCCESSFUL);
        }

        if (command.status().equals(FileStatusUpdaterCommand.Status.IMPORTING)) {
            status = new FileStatus(FileStatus.Status.IMPORTING);
        }

        if (command.status().equals(FileStatusUpdaterCommand.Status.IMPORT_SUCCESSFUL)) {
            status = new FileStatus(FileStatus.Status.IMPORT_SUCCESSFUL);
        }

        if (command.status().equals(FileStatusUpdaterCommand.Status.IMPORT_UNSUCCESSFUL)) {
            status = new FileStatus(FileStatus.Status.IMPORT_UNSUCCESSFUL);
        }

        if (status != null) {
            fileRepository.updateFileStatus(uuid, status, log);
            changeStatusToDelivery(uuid, status);
        }
    }

    private void changeStatusToDelivery(FileUUID uuid, FileStatus status) {

        if (status.importing() || status.importSuccessful() || status.importUnsuccessful()) {

            File file = fileRepository.findBy(uuid);
            if (file != null) {
                DeliveryId deliveryId = DeliveryId.fromValue(file.deliveryId().value());

                List<File> files = fileRepository.findByDeliveryId(deliveryId);
                int totalFiles = files.size();

                int totalImporting = (int) files.stream().filter(File::importing).count();
                int totalImportSuccessful = (int) files.stream().filter(File::importSuccessful).count();
                int totalImportUnsuccessful = (int) files.stream().filter(File::importUnSuccessful).count();

                if (totalImportSuccessful == totalFiles) {
                    deliveryRepository.changeStatus(deliveryId, new DeliveryStatus(DeliveryStatus.Status.SUCCESS_IMPORT));
                } else if (totalImportUnsuccessful > 0) {
                    deliveryRepository.changeStatus(deliveryId, new DeliveryStatus(DeliveryStatus.Status.FAILED_IMPORT));
                } else if (totalImporting > 0) {
                    deliveryRepository.changeStatus(deliveryId, new DeliveryStatus(DeliveryStatus.Status.IMPORTING));
                }

            }
        }


    }

}