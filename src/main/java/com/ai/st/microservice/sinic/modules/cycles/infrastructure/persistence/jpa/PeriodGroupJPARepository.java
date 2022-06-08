package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence.jpa;

import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodGroupEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PeriodGroupJPARepository
        extends PagingAndSortingRepository<PeriodGroupEntity, Long>, JpaSpecificationExecutor<PeriodGroupEntity> {

    long deleteByPeriodId(String period_id);

    List<PeriodGroupEntity> findByPeriodId(String uuid);

}
