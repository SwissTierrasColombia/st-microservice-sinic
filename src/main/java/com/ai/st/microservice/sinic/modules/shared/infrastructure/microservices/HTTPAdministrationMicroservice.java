package com.ai.st.microservice.sinic.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.UserFeignClient;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.sinic.modules.shared.domain.AdministrationUser;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;
import com.ai.st.microservice.sinic.modules.shared.domain.UserEmail;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.AdministrationMicroservice;
import org.springframework.stereotype.Service;

@Service
public class HTTPAdministrationMicroservice implements AdministrationMicroservice {

    private final UserFeignClient userFeignClient;

    public HTTPAdministrationMicroservice(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public AdministrationUser findAdministrationUser(UserCode userCode) {
        try {
            MicroserviceUserDto userDto = userFeignClient.findById(userCode.value());
            return new AdministrationUser(userCode, UserEmail.fromValue(userDto.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("Error while finding user with code: " + userCode.value(), e);
        }
    }

}
