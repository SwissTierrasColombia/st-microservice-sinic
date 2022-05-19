package com.ai.st.microservice.sinic.modules.groups.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.groups.domain.Group;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupId;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupName;
import com.ai.st.microservice.sinic.modules.groups.domain.contracts.GroupRepository;
import com.ai.st.microservice.sinic.modules.groups.infrastructure.persistence.jpa.GroupJPARepository;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.GroupEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public final class PostgresGroupRepository implements GroupRepository {

    private final GroupJPARepository groupJPARepository;

    public PostgresGroupRepository(GroupJPARepository groupJPARepository) {
        this.groupJPARepository = groupJPARepository;
    }

    @Override
    public Optional<Group> search(GroupId groupId) {
        GroupEntity groupEntity = groupJPARepository.findByUuid(groupId.value());
        return Optional.ofNullable(groupEntity).map(this::mapping);
    }

    @Override
    public Optional<Group> searchByName(GroupName groupName) {
        GroupEntity groupEntity = groupJPARepository.findByName(groupName.value());
        return Optional.ofNullable(groupEntity).map(this::mapping);
    }

    @Override
    public void save(Group group) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setUuid(group.id().value());
        groupEntity.setName(group.name().value());
        groupJPARepository.save(groupEntity);
    }

    @Override
    public void remove(GroupId groupId) {

    }

    @Override
    public void update(Group group) {
        GroupEntity groupEntity = groupJPARepository.findByUuid(group.id().value());
        if (groupEntity != null) {
            groupEntity.setName(group.name().value());
            groupJPARepository.save(groupEntity);
        }
    }

    @Override
    public List<Group> find() {
        return groupJPARepository.findAll().stream().map(this::mapping).collect(Collectors.toList());
    }

    private Group mapping(GroupEntity groupEntity) {
        return Group.fromPrimitives(groupEntity.getUuid(), groupEntity.getName());
    }

}
