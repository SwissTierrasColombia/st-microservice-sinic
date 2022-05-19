package com.ai.st.microservice.sinic.modules.groups.application;

import com.ai.st.microservice.sinic.modules.groups.domain.Group;
import com.ai.st.microservice.sinic.modules.shared.application.Response;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GroupResponse implements Response {

    public abstract String id();

    public abstract String name();

    public static GroupResponse from(Group group) {
        return GroupResponse.builder().id(group.id().value()).name(group.name().value()).build();
    }

    public static Builder builder() {
        return new AutoValue_GroupResponse.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract GroupResponse build();
    }
}
