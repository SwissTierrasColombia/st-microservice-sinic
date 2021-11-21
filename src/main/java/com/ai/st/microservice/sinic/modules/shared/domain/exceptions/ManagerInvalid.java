package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class ManagerInvalid extends DomainError {

    public ManagerInvalid(Long managerCode) {
        super("manager_code_invalid", String.format("El gestor con código '%d' no es válido", managerCode));
    }

}
