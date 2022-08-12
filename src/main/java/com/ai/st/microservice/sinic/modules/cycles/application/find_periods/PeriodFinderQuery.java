package com.ai.st.microservice.sinic.modules.cycles.application.find_periods;

import com.ai.st.microservice.sinic.modules.shared.application.Query;

public final class PeriodFinderQuery implements Query {

    private final String cycleId;

    public PeriodFinderQuery(String cycleId) {
        this.cycleId = cycleId;
    }

    public String cycleId() {
        return cycleId;
    }
}
