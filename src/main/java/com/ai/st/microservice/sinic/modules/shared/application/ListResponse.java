package com.ai.st.microservice.sinic.modules.shared.application;

import java.util.List;

public final class ListResponse<T> implements Response {

    private final List<T> items;

    public ListResponse(List<T> items) {
        this.items = items;
    }

    public List<T> list() {
        return items;
    }

    public int size() {
        return items.size();
    }

}
