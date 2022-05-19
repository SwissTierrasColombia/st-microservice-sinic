package com.ai.st.microservice.sinic.modules.periods.domain.duration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Duration {

    public abstract StartDate start();

    public abstract FinishDate finish();

    public static Builder builder() {
        return new AutoValue_Duration.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder start(StartDate start);

        public abstract Builder finish(FinishDate finish);

        public abstract Duration build();
    }

}
