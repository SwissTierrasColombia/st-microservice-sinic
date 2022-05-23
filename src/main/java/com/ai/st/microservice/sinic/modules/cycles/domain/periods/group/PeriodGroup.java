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

    public static Builder builder() {
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
