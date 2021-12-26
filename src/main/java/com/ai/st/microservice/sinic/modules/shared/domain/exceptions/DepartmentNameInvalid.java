package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DepartmentNameInvalid extends DomainError {

    public DepartmentNameInvalid(String name) {
        super("department_name_invalid", String.format("El nombre del departamento '%s' no es válido.", name));
    }

}
