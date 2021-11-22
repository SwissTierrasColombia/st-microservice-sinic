package com.ai.st.microservice.sinic.modules.files.application.add_file;

import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class FileAdder implements CommandUseCase<FileAdderCommand> {

    @Override
    public void handle(FileAdderCommand command) {

    }

}
