package com.ai.st.microservice.sinic.modules.cycles.domain.periods.group;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PeriodGroupId {

    public abstract String value();

    public static Builder builder() {
        return new AutoValue_PeriodGroupId.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract PeriodGroupId build();
    }

}
