package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileNotFound extends DomainError {

    public FileNotFound(Long fileId) {
        super("file_not_found", String.format("No se ha encontrado el adjunto '%d'.", fileId));
    }
}
