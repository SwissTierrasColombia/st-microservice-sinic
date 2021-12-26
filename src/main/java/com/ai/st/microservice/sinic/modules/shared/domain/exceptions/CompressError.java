package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class CompressError extends DomainError {

    public CompressError() {
        super("compress_file_error", "Ha ocurrido un error en el proceso de compresión de archivos.");
    }

    public CompressError(String errorMessage) {
        super("compress_file_error", String.format("Ha ocurrido un error en el proceso de compresión de archivos: %s", errorMessage));
    }

}
