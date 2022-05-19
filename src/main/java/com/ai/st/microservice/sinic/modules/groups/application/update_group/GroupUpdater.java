package com.ai.st.microservice.sinic.modules.groups.application.update_group;

import com.ai.st.microservice.sinic.modules.groups.domain.Group;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupId;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupName;
import com.ai.st.microservice.sinic.modules.groups.domain.contracts.GroupRepository;
import com.ai.st.microservice.sinic.modules.groups.domain.exceptions.GroupAlreadyExists;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class GroupUpdater implements CommandUseCase<GroupUpdaterCommand> {

    private final GroupRepository groupRepository;

    public GroupUpdater(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void handle(GroupUpdaterCommand command) {

        GroupId groupId = GroupId.of(command.groupId());
        GroupName groupName = GroupName.of(command.name());

        verifyIfGroupAlreadyExists(groupId, groupName);

        Group group = Group.create(groupId, groupName);

        groupRepository.update(group);
    }

    private void verifyIfGroupAlreadyExists(GroupId groupId, GroupName groupName) {
        groupRepository.searchByName(groupName).ifPresent(group -> {
            if (!group.id().equals(groupId)) {
                throw new GroupAlreadyExists(groupName.value());
            }
        });
    }

}
