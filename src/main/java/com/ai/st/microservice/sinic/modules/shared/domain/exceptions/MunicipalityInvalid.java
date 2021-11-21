package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public class MunicipalityInvalid extends DomainError {

    public MunicipalityInvalid(String municipalityCode) {
        super("municipality_code_invalid", String.format("El municipio '%s' no es válido.", municipalityCode));
    }
}
