package com.ai.st.microservice.sinic.modules.cycles.application.search_cycle;

import com.ai.st.microservice.sinic.modules.shared.application.Query;

public final class CycleSearcherQuery implements Query {

    private final String id;

    public CycleSearcherQuery(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
