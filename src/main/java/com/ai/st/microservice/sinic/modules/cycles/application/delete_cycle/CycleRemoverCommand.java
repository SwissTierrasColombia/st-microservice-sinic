package com.ai.st.microservice.sinic.modules.cycles.application.delete_cycle;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public class CycleRemoverCommand implements Command {

    private final String cycleId;

    public CycleRemoverCommand(String cycleId) {
        this.cycleId = cycleId;
    }

    public String cycleId() {
        return cycleId;
    }
}
