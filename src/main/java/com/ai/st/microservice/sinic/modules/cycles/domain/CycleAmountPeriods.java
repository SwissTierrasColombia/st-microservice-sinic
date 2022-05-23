package com.ai.st.microservice.sinic.modules.cycles.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CycleAmountPeriods {

    public abstract Integer value();

    public static CycleAmountPeriods of(Integer value) {
        if (value <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        return CycleAmountPeriods.builder().value(value).build();
    }

    private static Builder builder() {
        return new AutoValue_CycleAmountPeriods.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(Integer value);

        public abstract CycleAmountPeriods build();
    }

}
