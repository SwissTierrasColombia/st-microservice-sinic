package com.ai.st.microservice.sinic.modules.cycles.domain.periods.group;

import com.ai.st.microservice.sinic.modules.cycles.domain.periods.PeriodId;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.Duration;
import com.ai.st.microservice.sinic.modules.groups.domain.GroupId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PeriodGroup {

    public abstract PeriodGroupId id();

    public abstract GroupId groupId();

    public abstract PeriodId periodId();

    public abstract Duration duration();

    public static PeriodGroup create(PeriodGroupId id, GroupId groupId, PeriodId periodId, Duration duration) {
        return PeriodGroup.builder().id(id).groupId(groupId).periodId(periodId).duration(duration).build();
    }

    public static PeriodGroup create(GroupId groupId, PeriodId periodId, Duration duration) {
        return PeriodGroup.builder().id(PeriodGroupId.generate()).groupId(groupId).periodId(periodId).duration(duration)
                .build();
    }

    public boolean isBetweenPeriodDuration(Duration periodDuration) {
        final var startValidation = duration().start().value().after(periodDuration.finish().value())
                || duration().start().value().before(periodDuration.start().value());
        final var finishValidation = duration().finish().value().after(periodDuration.finish().value())
                || duration().finish().value().before(periodDuration.start().value());
        return !(startValidation || finishValidation);
    }

    private static Builder builder() {
        return new AutoValue_PeriodGroup.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(PeriodGroupId id);

        public abstract Builder groupId(GroupId groupId);

        public abstract Builder periodId(PeriodId periodId);

        public abstract Builder duration(Duration duration);

        public abstract PeriodGroup build();
    }

}
