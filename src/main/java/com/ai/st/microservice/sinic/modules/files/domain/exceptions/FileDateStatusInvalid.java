package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileDateStatusInvalid extends DomainError {

    public FileDateStatusInvalid(String date) {
        super("file_date_status_invalid", String.format("La fecha del estado '%s' es inválida", date));
    }

}
