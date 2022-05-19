package com.ai.st.microservice.sinic.modules.periods.domain.duration;

import com.google.auto.value.AutoValue;

import java.time.ZonedDateTime;

@AutoValue
public abstract class StartDate {

    public abstract ZonedDateTime value();

    public static Builder builder() {
        return new AutoValue_StartDate.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(ZonedDateTime value);

        public abstract StartDate build();
    }

}
