package com.ai.st.microservice.sinic.modules.shared.domain.criteria;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FieldUnsupported extends DomainError {

    public FieldUnsupported() {
        super("field_unsupported", "El campo a filtrar no esta soportado.");
    }
}
