package com.ai.st.microservice.sinic.modules.shared.domain;

public class AdministrationUser {

    private final UserCode code;
    private final UserEmail email;

    public AdministrationUser(UserCode code, UserEmail email) {
        this.code = code;
        this.email = email;
    }

    public UserCode code() {
        return code;
    }

    public UserEmail email() {
        return email;
    }
}
