package com.ai.st.microservice.sinic.modules.groups.infrastructure.persistence.jpa;

import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface GroupJPARepository
        extends PagingAndSortingRepository<GroupEntity, Long>, JpaSpecificationExecutor<GroupEntity> {

    GroupEntity findByName(String name);

    GroupEntity findByUuid(String uuid);

    List<GroupEntity> findAll();

    long deleteByName(String name);

}
