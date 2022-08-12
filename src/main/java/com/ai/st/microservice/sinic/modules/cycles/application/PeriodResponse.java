package com.ai.st.microservice.sinic.modules.cycles.application;

import com.ai.st.microservice.sinic.modules.cycles.domain.periods.Period;
import com.ai.st.microservice.sinic.modules.shared.application.Response;
import com.google.auto.value.AutoValue;

import java.util.List;
import java.util.stream.Collectors;

@AutoValue
public abstract class PeriodResponse implements Response {

    public abstract long startDate();

    public abstract long finishDate();

    public abstract List<GroupResponse> groups();

    public static PeriodResponse from(Period period) {
        return PeriodResponse.builder().startDate(period.duration().start().value().getTime())
                .finishDate(period.duration().finish().value().getTime())
                .groups(period.periodGroups().stream().map(GroupResponse::from).collect(Collectors.toList())).build();
    }

    private static Builder builder() {
        return new AutoValue_PeriodResponse.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder startDate(long startDate);

        public abstract Builder finishDate(long startDate);

        public abstract Builder groups(List<GroupResponse> groups);

        public abstract PeriodResponse build();
    }

}
