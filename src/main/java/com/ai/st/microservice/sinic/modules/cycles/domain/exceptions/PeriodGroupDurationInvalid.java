package com.ai.st.microservice.sinic.modules.cycles.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public class PeriodGroupDurationInvalid extends DomainError {

    public PeriodGroupDurationInvalid() {
        super("period_group_duration_invalid",
                "La duración del grupo debe estar en el rango de la duración del período");
    }
}
