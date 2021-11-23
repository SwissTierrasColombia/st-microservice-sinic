package com.ai.st.microservice.sinic.modules.files.application.update_file_status;

import com.ai.st.microservice.sinic.modules.files.domain.FileLog;
import com.ai.st.microservice.sinic.modules.files.domain.FileRepository;
import com.ai.st.microservice.sinic.modules.files.domain.FileStatus;
import com.ai.st.microservice.sinic.modules.files.domain.FileUUID;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class FileStatusUpdater implements CommandUseCase<FileStatusUpdaterCommand> {

    private final FileRepository fileRepository;

    public FileStatusUpdater(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void handle(FileStatusUpdaterCommand command) {

        FileUUID uuid = new FileUUID(command.fileUUID());

        FileStatus status;
        FileLog log = null;
        if (command.status().equals(FileStatusUpdaterCommand.Status.SUCCESSFUL)) {
            status = new FileStatus(FileStatus.Status.SUCCESSFUL);
        } else {
            log = FileLog.fromValue(command.log());
            status = new FileStatus(FileStatus.Status.UNSUCCESSFUL);
        }

        fileRepository.updateFileStatus(uuid, status, log);
    }

}
