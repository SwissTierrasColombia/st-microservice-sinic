package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileUrlInvalid extends DomainError {

    public FileUrlInvalid(String url) {
        super("file_url_invalid", String.format("La URL del archivo '%s' no es válida", url));
    }
}
