package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class MunicipalityNameInvalid extends DomainError {

    public MunicipalityNameInvalid(String name) {
        super("municipality_name_invalid", String.format("El nombre del municipio '%s' no es válido.", name));
    }

}
