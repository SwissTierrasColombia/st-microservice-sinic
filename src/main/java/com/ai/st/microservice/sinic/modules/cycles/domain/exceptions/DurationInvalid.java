package com.ai.st.microservice.sinic.modules.cycles.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DurationInvalid extends DomainError {

    public DurationInvalid(String startDate, String finishDate) {
        super("duration_invalid", String.format(
                "La fecha de inicio no puede ser mayor a la fecha de finalización: %s > %s", startDate, finishDate));
    }
}
