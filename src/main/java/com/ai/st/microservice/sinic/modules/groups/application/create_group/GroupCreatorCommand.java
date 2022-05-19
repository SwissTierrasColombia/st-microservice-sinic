package com.ai.st.microservice.sinic.modules.groups.application.create_group;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public class GroupCreatorCommand implements Command {

    private final String name;

    public GroupCreatorCommand(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
