package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.cycles.domain.*;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.CycleEntity;

public final class CycleMapper {

    public static Cycle from(CycleEntity cycleEntity) {
        return Cycle.create(CycleId.of(cycleEntity.getUuid()), CycleYear.of(cycleEntity.getYear()),
                CycleObservations.of(cycleEntity.getObservations()),
                CycleAmountPeriods.of(cycleEntity.getAmountPeriods()));
    }

}
