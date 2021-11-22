package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class StoreFileError extends DomainError {

    public StoreFileError() {
        super("store_file_error", "Ha ocurrido un error guardando el archivo.");
    }

    public StoreFileError(String message) {
        super("store_file_error", String.format("Ha ocurrido un error guardando el archivo '%s'.", message));
    }

}
