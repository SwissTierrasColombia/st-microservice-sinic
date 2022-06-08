package com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration;

import com.google.auto.value.AutoValue;

import java.sql.Timestamp;
import java.util.Date;

@AutoValue
public abstract class FinishDate {

    public abstract Date value();

    public static FinishDate of(long timestamp) {
        Timestamp ts = new Timestamp(timestamp);
        return FinishDate.builder().value(new Date(ts.getTime())).build();
    }

    private static Builder builder() {
        return new AutoValue_FinishDate.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(Date value);

        public abstract FinishDate build();
    }

}
