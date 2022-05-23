package com.ai.st.microservice.sinic.modules.cycles.domain;

import com.ai.st.microservice.sinic.modules.cycles.domain.periods.Period;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Cycle {

    public abstract CycleId id();

    public abstract CycleYear year();

    public abstract CycleObservations observations();

    public abstract CycleAmountPeriods amountPeriods();

    public abstract List<Period> periods();

    public static Cycle create(CycleId id, CycleYear year, CycleObservations observations,
            CycleAmountPeriods amountPeriods) {
        return Cycle.builder().id(id).year(year).observations(observations).periods(new ArrayList<>())
                .amountPeriods(amountPeriods).build();
    }

    public static Cycle create(CycleYear year, CycleObservations observations, CycleAmountPeriods amountPeriods) {
        return Cycle.builder().id(CycleId.generate()).year(year).periods(new ArrayList<>()).observations(observations)
                .amountPeriods(amountPeriods).build();
    }

    public static Builder builder() {
        return new AutoValue_Cycle.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(CycleId id);

        public abstract Builder year(CycleYear year);

        public abstract Builder observations(CycleObservations observations);

        public abstract Builder amountPeriods(CycleAmountPeriods amountPeriods);

        public abstract Builder periods(List<Period> periods);

        public abstract Cycle build();
    }

}
