package com.ai.st.microservice.sinic.modules.groups.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GroupId {

    public abstract String value();

    public static GroupId of(String value) {
        return GroupId.builder().value(value).build();
    }

    public static GroupId generate() {
        return GroupId.of(java.util.UUID.randomUUID().toString());
    }

    private static Builder builder() {
        return new AutoValue_GroupId.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder value(String value);

        public abstract GroupId build();
    }

}
