package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerName;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DeliveryManager {

    public abstract ManagerCode code();

    public abstract ManagerName name();

    public static DeliveryManager.Builder builder() {
        return new AutoValue_DeliveryManager.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder code(ManagerCode code);

        public abstract Builder name(ManagerName name);

        public abstract DeliveryManager build();
    }

}
