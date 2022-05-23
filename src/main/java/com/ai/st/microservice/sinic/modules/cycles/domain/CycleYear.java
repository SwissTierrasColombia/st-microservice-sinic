package com.ai.st.microservice.sinic.modules.cycles.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CycleYear {

    public abstract Integer value();

    public static CycleYear of(Integer value) {
        return CycleYear.builder().value(value).build();
    }

    private static Builder builder() {
        return new AutoValue_CycleYear.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(Integer value);

        public abstract CycleYear build();
    }

}
