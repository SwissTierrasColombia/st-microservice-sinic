package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileStatusInvalid extends DomainError {

    public FileStatusInvalid(String status) {
        super("file_status_invalid", String.format("El estado del archivo '%s' no es válido.", status));
    }

}
