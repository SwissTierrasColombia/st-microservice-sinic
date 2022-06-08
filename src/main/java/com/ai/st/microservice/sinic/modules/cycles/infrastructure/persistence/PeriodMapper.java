package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.cycles.domain.CycleId;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.Period;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.PeriodId;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.Duration;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.FinishDate;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.StartDate;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.group.PeriodGroup;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodGroupEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class PeriodMapper {

    public static Period from(PeriodEntity periodEntity) {

        final var periodId = PeriodId.of(periodEntity.getUuid());
        final var duration = Duration.create(StartDate.of(periodEntity.getDateStart().getTime()),
                FinishDate.of(periodEntity.getDateFinish().getTime()));
        final var cycleId = CycleId.of(periodEntity.getCycleId());
        final var periodGroups = mapperPeriodGroups(periodEntity.getPeriodGroups());

        return Period.create(periodId, cycleId, duration, periodGroups);
    }

    private static List<PeriodGroup> mapperPeriodGroups(List<PeriodGroupEntity> periodGroupEntities) {
        return periodGroupEntities.stream().map(PeriodGroupMapper::from).collect(Collectors.toList());
    }

}
