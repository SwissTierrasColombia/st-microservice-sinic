package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.cycles.domain.*;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.Period;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.CycleEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CycleMapper {

    public static Cycle from(CycleEntity cycleEntity) {

        final var periods = mapperPeriods(cycleEntity.getPeriods());

        return Cycle.create(CycleId.of(cycleEntity.getUuid()), CycleYear.of(cycleEntity.getYear()),
                CycleObservations.of(cycleEntity.getObservations()),
                CycleAmountPeriods.of(cycleEntity.getAmountPeriods()), periods);
    }

    private static List<Period> mapperPeriods(List<PeriodEntity> periodEntities) {
        return periodEntities.stream().map(PeriodMapper::from).collect(Collectors.toList());
    }

}
