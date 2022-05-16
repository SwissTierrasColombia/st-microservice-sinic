package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class UserNotFound extends DomainError {

    public UserNotFound(Long userCode) {
        super("user_not_found", String.format("No se ha encontrado el usuario '%d'.", userCode));
    }
}
