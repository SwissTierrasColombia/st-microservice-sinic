package com.ai.st.microservice.sinic.modules.cycles.application;

import com.ai.st.microservice.sinic.modules.cycles.domain.Cycle;
import com.ai.st.microservice.sinic.modules.shared.application.Response;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CycleResponse implements Response {

    public abstract String id();

    public abstract Integer year();

    public abstract Boolean status();

    public abstract Integer amountPeriods();

    public abstract String observations();

    public static CycleResponse from(Cycle cycle) {
        return CycleResponse.builder().id(cycle.id().value()).year(cycle.year().value()).status(cycle.status().value())
                .amountPeriods(cycle.amountPeriods().value()).observations(cycle.observations().value()).build();
    }

    private static Builder builder() {
        return new AutoValue_CycleResponse.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);

        public abstract Builder year(Integer year);

        public abstract Builder amountPeriods(Integer observations);

        public abstract Builder observations(String observations);

        public abstract Builder status(Boolean status);

        public abstract CycleResponse build();
    }

}
