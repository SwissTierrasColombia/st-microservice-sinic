package com.ai.st.microservice.sinic.modules.cycles.application;

import com.ai.st.microservice.sinic.modules.cycles.domain.periods.group.PeriodGroup;
import com.ai.st.microservice.sinic.modules.shared.application.Response;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GroupResponse implements Response {

    public abstract String groupId();

    public abstract long startDate();

    public abstract long finishDate();

    public static GroupResponse from(PeriodGroup group) {
        return GroupResponse.builder().groupId(group.groupId().value())
                .startDate(group.duration().start().value().getTime())
                .finishDate(group.duration().finish().value().getTime()).build();
    }

    private static Builder builder() {
        return new AutoValue_GroupResponse.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder groupId(String groupId);

        public abstract Builder startDate(long startDate);

        public abstract Builder finishDate(long startDate);

        public abstract GroupResponse build();
    }

}
