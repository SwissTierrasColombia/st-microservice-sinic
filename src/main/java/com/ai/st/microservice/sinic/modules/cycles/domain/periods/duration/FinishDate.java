package com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration;

import com.google.auto.value.AutoValue;

import java.time.ZonedDateTime;

@AutoValue
public abstract class FinishDate {

    public abstract ZonedDateTime value();

    public static Builder builder() {
        return new AutoValue_FinishDate.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(ZonedDateTime value);

        public abstract FinishDate build();
    }

}
