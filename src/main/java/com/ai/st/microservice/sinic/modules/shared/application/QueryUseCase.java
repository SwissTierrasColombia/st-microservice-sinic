package com.ai.st.microservice.sinic.modules.shared.application;

public interface QueryUseCase<Q extends Query, R extends Response> {

    R handle(Q query);

}
