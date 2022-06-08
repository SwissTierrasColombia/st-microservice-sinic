package com.ai.st.microservice.sinic.modules.cycles.domain.periods.duration;

import com.ai.st.microservice.sinic.modules.cycles.domain.exceptions.DurationInvalid;
import com.google.auto.value.AutoValue;

import java.time.format.DateTimeFormatter;

@AutoValue
public abstract class Duration {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");

    public abstract StartDate start();

    public abstract FinishDate finish();

    public static Duration create(StartDate start, FinishDate finish) {

        if (start.value().after(finish.value())) {
            String startDate = start.value().toString();
            String finishDate = finish.value().toString();
            throw new DurationInvalid(startDate, finishDate);
        }

        return Duration.builder().start(start).finish(finish).build();
    }

    private static Builder builder() {
        return new AutoValue_Duration.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder start(StartDate start);

        public abstract Builder finish(FinishDate finish);

        public abstract Duration build();
    }

}
