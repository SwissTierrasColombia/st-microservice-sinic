package com.ai.st.microservice.sinic.modules.cycles.domain.periods;

import com.ai.st.microservice.sinic.modules.cycles.domain.CycleId;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration.Duration;
import com.ai.st.microservice.sinic.modules.cycles.domain.periods.group.PeriodGroup;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Period {

    public abstract PeriodId id();

    public abstract Duration duration();

    public abstract CycleId cycleId();

    public abstract List<PeriodGroup> periodGroups();

    public static Period create(PeriodId id, CycleId cycleId, Duration duration, List<PeriodGroup> periodGroups) {
        return Period.builder().id(id).duration(duration).cycleId(cycleId).periodGroups(periodGroups).build();
    }

    private static Builder builder() {
        return new AutoValue_Period.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(PeriodId id);

        public abstract Builder duration(Duration duration);

        public abstract Builder cycleId(CycleId cycleId);

        public abstract Builder periodGroups(List<PeriodGroup> periodGroups);

        public abstract Period build();
    }

}
