package com.ai.st.microservice.sinic.modules.cycles.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class UnauthorizedRemoveCycle extends DomainError {

    public UnauthorizedRemoveCycle(Integer year) {
        super("unauthorized_remove_cycle",
                String.format("No se puede eliminar el ciclo para el año %d porque se encuentra activo", year));
    }
}
