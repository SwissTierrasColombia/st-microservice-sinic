package com.ai.st.microservice.sinic.modules.periods.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PeriodYear {

    public abstract Integer value();

    public static Builder builder() {
        return new AutoValue_PeriodYear.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(Integer value);

        public abstract PeriodYear build();
    }

}
