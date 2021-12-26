package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class ManagerNotFound extends DomainError {

    public ManagerNotFound(Long managerCode) {
        super("manager_not_found", String.format("No se ha encontrado el gestor '%d'.", managerCode));
    }
}
