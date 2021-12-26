package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileObservationsInvalid extends DomainError {

    public FileObservationsInvalid(String observations) {
        super("file_observations_invalid", String.format("Las observaciones '%s' no son válidas.", observations));
    }

}
