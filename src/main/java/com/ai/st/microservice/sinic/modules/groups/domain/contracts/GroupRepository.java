package com.ai.st.microservice.sinic.modules.groups.domain.contracts;

import com.ai.st.microservice.sinic.modules.groups.domain.Group;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupId;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupName;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {

    Optional<Group> search(GroupId groupId);

    Optional<Group> searchByName(GroupName groupName);

    void save(Group group);

    void remove(GroupId groupId);

    void update(Group group);

    List<Group> find();

}
