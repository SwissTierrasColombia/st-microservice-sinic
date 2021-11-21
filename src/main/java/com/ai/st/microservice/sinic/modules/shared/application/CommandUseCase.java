package com.ai.st.microservice.sinic.modules.shared.application;

public interface CommandUseCase<C extends Command> {

    void handle(C command);

}
