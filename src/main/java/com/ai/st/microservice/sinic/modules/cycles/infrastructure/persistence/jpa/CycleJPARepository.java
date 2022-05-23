package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence.jpa;

import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.CycleEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CycleJPARepository
        extends PagingAndSortingRepository<CycleEntity, Long>, JpaSpecificationExecutor<CycleEntity> {

    CycleEntity findByUuid(String uuid);

    CycleEntity findByYear(Integer year);

    List<CycleEntity> findAll();

}
