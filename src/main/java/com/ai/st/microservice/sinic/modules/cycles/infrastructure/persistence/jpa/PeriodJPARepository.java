package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence.jpa;

import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PeriodJPARepository
        extends PagingAndSortingRepository<PeriodEntity, Long>, JpaSpecificationExecutor<PeriodEntity> {

    long deleteByCycleId(String cycleId);

    List<PeriodEntity> findByCycleId(String uuid);

}
