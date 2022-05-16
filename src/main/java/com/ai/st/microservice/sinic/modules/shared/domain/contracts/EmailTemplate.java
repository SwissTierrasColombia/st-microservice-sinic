package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import java.util.Map;

public interface EmailTemplate {

    String parse(String template, Map<String, Object> data);

}
