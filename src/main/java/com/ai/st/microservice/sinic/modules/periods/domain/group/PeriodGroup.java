package com.ai.st.microservice.sinic.modules.periods.domain.group;

import com.ai.st.microservice.sinic.modules.groups.domain.Group;
import com.ai.st.microservice.sinic.modules.periods.domain.duration.Duration;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PeriodGroup {

    public abstract Group group();

    public abstract Duration duration();

    public static Builder builder() {
        return new AutoValue_PeriodGroup.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder group(Group group);

        public abstract Builder duration(Duration duration);

        public abstract PeriodGroup build();
    }

}
