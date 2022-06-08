package com.ai.st.microservice.sinic.modules.cycles.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.cycles.domain.periods.PeriodId;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.Duration;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.FinishDate;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.StartDate;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.group.PeriodGroup;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.group.PeriodGroupId;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupId;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.PeriodGroupEntity;

public final class PeriodGroupMapper {

    public static PeriodGroup from(PeriodGroupEntity periodGroupEntity) {

        final var periodGroupId = PeriodGroupId.of(periodGroupEntity.getUuid());
        final var groupId = GroupId.of(periodGroupEntity.getGroupId());
        final var periodId = PeriodId.of(periodGroupEntity.getPeriodId());

        final var duration = Duration.create(StartDate.of(periodGroupEntity.getDateStart().getTime()),
                FinishDate.of(periodGroupEntity.getDateFinish().getTime()));

        return PeriodGroup.create(periodGroupId, groupId, periodId, duration);
    }

}
