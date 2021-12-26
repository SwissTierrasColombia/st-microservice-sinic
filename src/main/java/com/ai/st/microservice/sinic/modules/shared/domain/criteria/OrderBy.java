package com.ai.st.microservice.sinic.modules.shared.domain.criteria;

public final class OrderBy {

    private final String value;

    public OrderBy(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
