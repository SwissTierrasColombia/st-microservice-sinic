package com.ai.st.microservice.sinic.modules.shared.application;

public final class StringResponse implements Response {

    private final String value;

    public StringResponse(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    
}
