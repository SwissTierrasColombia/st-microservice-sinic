package com.ai.st.microservice.sinic.modules.cycles.application.create_cycle;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class CycleCreatorCommand implements Command {

    private final int year;
    private final String observations;
    private final int amountPeriods;

    public CycleCreatorCommand(int year, String observations, int amountPeriods) {
        this.year = year;
        this.observations = observations;
        this.amountPeriods = amountPeriods;
    }

    public int year() {
        return year;
    }

    public String observations() {
        return observations;
    }

    public int amountPeriods() {
        return amountPeriods;
    }

}
