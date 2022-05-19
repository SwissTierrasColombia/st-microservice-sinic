package com.ai.st.microservice.sinic.modules.groups.application.update_group;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public class GroupUpdaterCommand implements Command {

    private final String groupId;
    private final String name;

    public GroupUpdaterCommand(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public String groupId() {
        return groupId;
    }

    public String name() {
        return name;
    }
}
