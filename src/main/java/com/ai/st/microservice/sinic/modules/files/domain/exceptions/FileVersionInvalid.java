package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileVersionInvalid extends DomainError {

    public FileVersionInvalid(String version) {
        super("file_version_invalid", String.format("La versión del archivo '%s' no es válida.", version));
    }
}
