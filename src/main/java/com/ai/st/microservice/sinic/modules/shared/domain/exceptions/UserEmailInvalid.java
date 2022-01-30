package com.ai.st.microservice.sinic.modules.shared.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public class UserEmailInvalid extends DomainError {

    public UserEmailInvalid(String email) {
        super("user_email_invalid", String.format("El email '%s' no es válido.", email));
    }
}
