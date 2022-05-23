package com.ai.st.microservice.sinic.modules.cycles.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CycleObservations {

    public abstract String value();

    public static CycleObservations of(String value) {
        return CycleObservations.builder().value(value).build();
    }

    private static Builder builder() {
        return new AutoValue_CycleObservations.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract CycleObservations build();
    }

}
