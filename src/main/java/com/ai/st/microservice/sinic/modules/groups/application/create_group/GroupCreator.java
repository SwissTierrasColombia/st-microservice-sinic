package com.ai.st.microservice.sinic.modules.groups.application.create_group;

import com.ai.st.microservice.sinic.modules.groups.domain.Group;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupId;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupName;
import com.ai.st.microservice.sinic.modules.groups.domain.contracts.GroupRepository;
import com.ai.st.microservice.sinic.modules.groups.domain.exceptions.GroupAlreadyExists;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class GroupCreator implements CommandUseCase<GroupCreatorCommand> {

    private final GroupRepository groupRepository;

    public GroupCreator(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void handle(GroupCreatorCommand command) {

        GroupId groupId = GroupId.generate();
        GroupName groupName = GroupName.of(command.name());

        verifyIfGroupAlreadyExists(groupName);

        Group group = Group.create(groupId, groupName);

        groupRepository.save(group);
    }

    private void verifyIfGroupAlreadyExists(GroupName groupName) {
        groupRepository.searchByName(groupName).ifPresent(s -> {
            throw new GroupAlreadyExists(groupName.value());
        });
    }

}
