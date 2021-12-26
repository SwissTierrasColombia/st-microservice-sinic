package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class NumberFilesExceeded extends DomainError {

    public NumberFilesExceeded(int maximumAttachments) {
        super("number_files_exceeded", String.format("Máximo se puede cargar %d adjuntos por entrega.", maximumAttachments));
    }

}
