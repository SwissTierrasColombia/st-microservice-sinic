package com.ai.st.microservice.sinic.modules.files.application.remove_file;

import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class FileRemover implements CommandUseCase<FileRemoverCommand> {

    @Override
    public void handle(FileRemoverCommand command) {

    }

}
