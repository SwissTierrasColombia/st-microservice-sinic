package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileDateInvalid extends DomainError {

    public FileDateInvalid(String date) {
        super("file_date_invalid", String.format("La fecha del archivo '%s' es inválida", date));
    }

}
