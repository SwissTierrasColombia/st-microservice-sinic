package com.ai.st.microservice.sinic.modules.deliveries.application.find_deliveries;

import com.ai.st.microservice.sinic.modules.shared.application.Query;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;

import java.util.ArrayList;
import java.util.List;

public final class DeliveriesFinderQuery implements Query {

    private final int page;
    private final int limit;
    private final List<String> states;
    private final String code;
    private final String municipality;
    private final Long manager;
    private final Roles role;
    private final Long entityCode;
    private final Long userCode;

    public DeliveriesFinderQuery(int page, int limit, List<String> states, String code, String municipality, Long manager, Roles role, Long entityCode, Long userCode) {
        this.page = page;
        this.limit = limit;
        this.states = (states == null) ? new ArrayList<>() : states;
        this.code = code;
        this.municipality = municipality;
        this.manager = manager;
        this.role = role;
        this.entityCode = entityCode;
        this.userCode = userCode;
    }

    public int page() {
        return page;
    }

    public int limit() {
        return limit;
    }

    public List<String> states() {
        return states;
    }

    public String code() {
        return code;
    }

    public String municipality() {
        return municipality;
    }

    public Roles role() {
        return role;
    }

    public Long entityCode() {
        return entityCode;
    }

    public Long userCode() {
        return userCode;
    }

    public Long manager() {
        return manager;
    }

}
