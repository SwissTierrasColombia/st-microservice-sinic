package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.shared.domain.DepartmentName;
import com.ai.st.microservice.sinic.modules.shared.domain.MunicipalityCode;
import com.ai.st.microservice.sinic.modules.shared.domain.MunicipalityName;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DeliveryLocality {

    public abstract MunicipalityCode code();

    public abstract MunicipalityName municipality();

    public abstract DepartmentName department();

    public static DeliveryLocality.Builder builder() {
        return new AutoValue_DeliveryLocality.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder code(MunicipalityCode code);

        public abstract Builder municipality(MunicipalityName name);

        public abstract Builder department(DepartmentName department);

        public abstract DeliveryLocality build();
    }

}
