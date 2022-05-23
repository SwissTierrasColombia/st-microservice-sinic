package com.ai.st.microservice.sinic.modules.cycles.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CycleId {

    public abstract String value();

    public static CycleId of(String value) {
        return CycleId.builder().value(value).build();
    }

    public static CycleId generate() {
        return CycleId.of(java.util.UUID.randomUUID().toString());
    }

    private static Builder builder() {
        return new AutoValue_CycleId.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract CycleId build();
    }

}
