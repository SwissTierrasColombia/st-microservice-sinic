package com.ai.st.microservice.sinic.modules.groups.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GroupName {

    public abstract String value();

    public static GroupName of(String value) {
        return GroupName.builder().value(value.toUpperCase()).build();
    }

    public static Builder builder() {
        return new AutoValue_GroupName.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract GroupName build();
    }

}
