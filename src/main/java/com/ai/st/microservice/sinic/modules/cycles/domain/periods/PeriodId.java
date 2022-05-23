package com.ai.st.microservice.sinic.modules.cycles.domain.periods;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PeriodId {

    public abstract String value();

    public static Builder builder() {
        return new AutoValue_PeriodId.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract PeriodId build();
    }

}
