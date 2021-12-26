package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class UserInvalid extends DomainError {

    public UserInvalid(Long userCode) {
        super("user_code_invalid", String.format("El usuario '%d' no es válido", userCode));
    }
}
