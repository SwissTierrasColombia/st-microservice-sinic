package com.ai.st.microservice.sinic.modules.groups.application.find_groups;

import com.ai.st.microservice.sinic.modules.groups.application.GroupResponse;
import com.ai.st.microservice.sinic.modules.groups.domain.contracts.GroupRepository;
import com.ai.st.microservice.sinic.modules.shared.application.ListResponse;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class GroupFinder implements QueryUseCase<GroupFinderQuery, ListResponse<GroupResponse>> {

    private final GroupRepository groupRepository;

    public GroupFinder(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public ListResponse<GroupResponse> handle(GroupFinderQuery query) {
        return new ListResponse<>(
                groupRepository.find().stream().map(GroupResponse::from).collect(Collectors.toList()));
    }

}
