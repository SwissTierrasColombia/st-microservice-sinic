package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class ManagerDoesNotBelongToMunicipality extends DomainError {

    public ManagerDoesNotBelongToMunicipality(Long managerCode, String municipalityCode) {
        super("manager_does_not_belong_to_municipality",
                String.format("El gestor '%d' no pertenece al municipio '%s'.", managerCode, municipalityCode));
    }
}
