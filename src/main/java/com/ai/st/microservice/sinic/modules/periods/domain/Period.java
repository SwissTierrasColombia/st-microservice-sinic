package com.ai.st.microservice.sinic.modules.periods.domain;

import com.ai.st.microservice.sinic.modules.periods.domain.duration.Duration;
import com.ai.st.microservice.sinic.modules.periods.domain.group.PeriodGroup;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Period {

    public abstract PeriodId id();

    public abstract PeriodYear year();

    public abstract Duration duration();

    public abstract List<PeriodGroup> periodGroups();

    public static Builder builder() {
        return new AutoValue_Period.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(PeriodId id);

        public abstract Builder year(PeriodYear year);

        public abstract Builder duration(Duration duration);

        public abstract Builder periodGroups(List<PeriodGroup> periodGroups);

        public abstract Period build();
    }

}
