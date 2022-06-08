package com.ai.st.microservice.sinic.modules.cycles.domain.periods;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PeriodId {

    public abstract String value();

    public static PeriodId of(String value) {
        return PeriodId.builder().value(value).build();
    }

    public static PeriodId generate() {
        return PeriodId.of(java.util.UUID.randomUUID().toString());
    }

    private static Builder builder() {
        return new AutoValue_PeriodId.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract PeriodId build();
    }

}
