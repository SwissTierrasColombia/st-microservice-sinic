package com.ai.st.microservice.sinic.modules.groups.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class GroupDoesNotExists extends DomainError {

    public GroupDoesNotExists(String groupName) {
        super("group_does_exists", String.format("El grupo %s no existe", groupName));
    }
}
