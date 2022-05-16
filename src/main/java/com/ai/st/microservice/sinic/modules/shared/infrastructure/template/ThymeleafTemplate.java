package com.ai.st.microservice.sinic.modules.shared.infrastructure.template;

import com.ai.st.microservice.sinic.modules.shared.domain.contracts.EmailTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public final class ThymeleafTemplate implements EmailTemplate {

    private final TemplateEngine templateEngine;

    public ThymeleafTemplate(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String parse(String template, Map<String, Object> data) {
        Context context = new Context();
        data.forEach(context::setVariable);
        return templateEngine.process(template, context);
    }

}
