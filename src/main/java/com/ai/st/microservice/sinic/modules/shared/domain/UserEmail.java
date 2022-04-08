package com.ai.st.microservice.sinic.modules.shared.domain;

import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.UserEmailInvalid;

public final class UserEmail extends StringValueObject {

    private UserEmail(String value) {
        super(value);
    }

    private static void ensureEmail(String value) {
        if (value == null || value.isEmpty())
            throw new UserEmailInvalid(value);
    }

    public static UserEmail fromValue(String value) {
        ensureEmail(value);
        return new UserEmail(value);
    }

}
