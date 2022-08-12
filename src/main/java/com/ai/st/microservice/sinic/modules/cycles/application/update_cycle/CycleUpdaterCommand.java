package com.ai.st.microservice.sinic.modules.cycles.application.update_cycle;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public class CycleUpdaterCommand implements Command {

    private final String cycleId;
    private final Integer periodsAmount;
    private final String observations;
    private final Boolean status;

    public CycleUpdaterCommand(String cycleId, Integer periodsAmount, String observations, Boolean status) {
        this.cycleId = cycleId;
        this.periodsAmount = periodsAmount;
        this.observations = observations;
        this.status = status;
    }

    public String cycleId() {
        return cycleId;
    }

    public Integer periodsAmount() {
        return periodsAmount;
    }

    public String observations() {
        return observations;
    }

    public Boolean status() {
        return status;
    }
}
