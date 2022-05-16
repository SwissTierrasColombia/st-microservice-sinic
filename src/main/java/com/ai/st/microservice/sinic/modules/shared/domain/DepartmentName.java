package com.ai.st.microservice.sinic.modules.shared.domain;

import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.DepartmentNameInvalid;

public final class DepartmentName extends StringValueObject {

    private DepartmentName(String value) {
        super(value);
    }

    private static void ensureName(String value) {
        if (value == null || value.isEmpty())
            throw new DepartmentNameInvalid(value);
    }

    public static DepartmentName fromValue(String value) {
        ensureName(value);
        return new DepartmentName(value);
    }

}
