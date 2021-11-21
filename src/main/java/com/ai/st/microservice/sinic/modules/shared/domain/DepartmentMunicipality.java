package com.ai.st.microservice.sinic.modules.shared.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DepartmentMunicipality {

    public abstract DepartmentName department();

    public abstract MunicipalityName municipality();

    public static DepartmentMunicipality.Builder builder() {
        return new AutoValue_DepartmentMunicipality.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder department(DepartmentName department);

        public abstract Builder municipality(MunicipalityName municipality);

        public abstract DepartmentMunicipality build();
    }

}
