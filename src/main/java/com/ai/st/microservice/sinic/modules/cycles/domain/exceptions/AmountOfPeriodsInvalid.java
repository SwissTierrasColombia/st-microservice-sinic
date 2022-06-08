package com.ai.st.microservice.sinic.modules.cycles.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public class AmountOfPeriodsInvalid extends DomainError {

    public AmountOfPeriodsInvalid(int amountPeriods) {
        super("number_of_periods_invalid",
                String.format("La cantidad de períodos debe ser la que se configuró en el ciclo es %d", amountPeriods));
    }
}
