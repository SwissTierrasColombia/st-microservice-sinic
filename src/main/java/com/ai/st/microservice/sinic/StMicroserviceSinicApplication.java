package com.ai.st.microservice.sinic;

import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients(basePackages = { "com.ai.st.microservice.common.clients" })
@EnableEurekaClient
@ComponentScan(value = { "com.ai.st.microservice.common.business",
        "com.ai.st.microservice.sinic" }, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class))
public class StMicroserviceSinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(StMicroserviceSinicApplication.class, args);
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

}
