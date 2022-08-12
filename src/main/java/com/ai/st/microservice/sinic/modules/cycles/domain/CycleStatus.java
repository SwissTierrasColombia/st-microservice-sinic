package com.ai.st.microservice.sinic.modules.cycles.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CycleStatus {

    public abstract Boolean value();

    public static CycleStatus of(Boolean value) {
        return CycleStatus.builder().value(value).build();
    }

    private static Builder builder() {
        return new AutoValue_CycleStatus.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(Boolean value);

        public abstract CycleStatus build();
    }

}
