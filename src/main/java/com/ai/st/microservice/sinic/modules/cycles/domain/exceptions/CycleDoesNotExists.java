package com.ai.st.microservice.sinic.modules.cycles.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class CycleDoesNotExists extends DomainError {

    public CycleDoesNotExists(String id) {
        super("cycle_does_not_exists", String.format("El ciclo %s no existe", id));
    }
}
