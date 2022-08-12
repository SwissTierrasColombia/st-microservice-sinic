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

    public abstract CycleStatus status();

    public abstract CycleAmountPeriods amountPeriods();

    public abstract List<Period> periods();

    public static Cycle create(CycleId id, CycleYear year, CycleObservations observations,
            CycleAmountPeriods amountPeriods, List<Period> periods, CycleStatus status) {
        return Cycle.builder().id(id).year(year).observations(observations).periods(periods).status(status)
                .amountPeriods(amountPeriods).build();
    }

    public static Cycle create(CycleYear year, CycleObservations observations, CycleAmountPeriods amountPeriods) {
        return Cycle.builder().id(CycleId.generate()).year(year).status(CycleStatus.of(Boolean.FALSE))
                .periods(new ArrayList<>()).observations(observations).amountPeriods(amountPeriods).build();
    }

    public Cycle withStatus(CycleStatus status) {
        return create(this.id(), this.year(), this.observations(), this.amountPeriods(), this.periods(), status);
    }

    public Cycle withPeriods(List<Period> periods) {
        return create(this.id(), this.year(), this.observations(), this.amountPeriods(), periods, this.status());
    }

    public Cycle withObservations(CycleObservations observations) {
        return create(this.id(), this.year(), observations, this.amountPeriods(), this.periods(), this.status());
    }

    public Cycle withAmountPeriods(CycleAmountPeriods amountPeriods) {
        return create(this.id(), this.year(), this.observations(), amountPeriods, this.periods(), this.status());
    }

    public boolean isActive() {
        return this.status().value();
    }

    public void setPeriods(List<Period> periods) {
        periods().clear();
        periods.forEach(this::addPeriod);
    }

    public void addPeriod(Period period) {
        this.periods().add(period);
    }

    public static Builder builder() {
        return new AutoValue_Cycle.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(CycleId id);

        public abstract Builder year(CycleYear year);

        public abstract Builder observations(CycleObservations observations);

        public abstract Builder status(CycleStatus observations);

        public abstract Builder amountPeriods(CycleAmountPeriods amountPeriods);

        public abstract Builder periods(List<Period> periods);

        public abstract Cycle build();
    }

}
