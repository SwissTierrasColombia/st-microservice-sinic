package com.ai.st.microservice.sinic.modules.shared.domain.criteria;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class OperatorUnsupported extends DomainError {

    public OperatorUnsupported() {
        super("operator_unsupported", "Operador no soportado.");
    }
}
