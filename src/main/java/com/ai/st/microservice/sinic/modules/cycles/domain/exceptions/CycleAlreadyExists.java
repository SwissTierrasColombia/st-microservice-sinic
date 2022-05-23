package com.ai.st.microservice.sinic.modules.cycles.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class CycleAlreadyExists extends DomainError {

    public CycleAlreadyExists(Integer year) {
        super("cycle_already_exists", String.format("Ya existe un ciclo para el año %d", year));
    }
}
