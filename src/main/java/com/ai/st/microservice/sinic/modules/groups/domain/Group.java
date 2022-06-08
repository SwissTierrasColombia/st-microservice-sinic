package com.ai.st.microservice.sinic.modules.groups.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Group {

    public abstract GroupId id();

    public abstract GroupName name();

    public static Group create(GroupId id, GroupName name) {
        return Group.builder().id(id).name(name).build();
    }

    public static Group fromPrimitives(String id, String name) {
        return Group.builder().name(GroupName.builder().value(name).build()).id(GroupId.of(id)).build();
    }

    public static Builder builder() {
        return new AutoValue_Group.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(GroupId id);

        public abstract Builder name(GroupName name);

        public abstract Group build();
    }

}
