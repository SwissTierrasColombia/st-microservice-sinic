package com.ai.st.microservice.sinic.modules.cycles.domain.periods.group;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PeriodGroupId {

    public abstract String value();

    public static PeriodGroupId of(String value) {
        return PeriodGroupId.builder().value(value).build();
    }

    public static PeriodGroupId generate() {
        return PeriodGroupId.of(java.util.UUID.randomUUID().toString());
    }

    private static Builder builder() {
        return new AutoValue_PeriodGroupId.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract PeriodGroupId build();
    }

}
