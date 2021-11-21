package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class ManagerNameInvalid extends DomainError {

    public ManagerNameInvalid(String name) {
        super("manager_name_invalid", String.format("El nombre del gestor '%s' no es válido.", name));
    }

}
