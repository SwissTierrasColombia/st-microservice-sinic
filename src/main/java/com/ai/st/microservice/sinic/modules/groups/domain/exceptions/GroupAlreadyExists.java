package com.ai.st.microservice.sinic.modules.groups.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class GroupAlreadyExists extends DomainError {

    public GroupAlreadyExists(String groupName) {
        super("group_already_exists", String.format("El grupo %s ya se encuentra registrado", groupName));
    }
}
