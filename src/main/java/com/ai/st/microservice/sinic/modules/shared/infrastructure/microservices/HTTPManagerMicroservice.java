package com.ai.st.microservice.sinic.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.ManagerFeignClient;
import com.ai.st.microservice.common.clients.UserFeignClient;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;

import com.ai.st.microservice.common.dto.managers.MicroserviceManagerUserDto;
import com.ai.st.microservice.sinic.modules.shared.domain.*;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.ManagerMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.ManagerNotFound;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.UserNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class HTTPManagerMicroservice implements ManagerMicroservice {

    private final ManagerFeignClient managerFeignClient;
    private final UserFeignClient userFeignClient;

    public HTTPManagerMicroservice(ManagerFeignClient managerFeignClient, UserFeignClient userFeignClient) {
        this.managerFeignClient = managerFeignClient;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public ManagerName getManagerName(ManagerCode managerCode) {
        MicroserviceManagerDto managerDto = managerFeignClient.findById(managerCode.value());

        if (managerDto == null) {
            throw new ManagerNotFound(managerCode.value());
        }

        return ManagerName.fromValue(managerDto.getName());
    }

    @Override
    public AdministrationUser findSinicUser(ManagerCode managerCode) {

        Long sinicProfile = (long) 2;
        List<MicroserviceManagerUserDto> usersByManager = managerFeignClient.findUsersByManager(managerCode.value(),
                List.of(sinicProfile));

        for (MicroserviceManagerUserDto userDto : usersByManager) {
            MicroserviceUserDto userFound = userFeignClient.findById(userDto.getUserCode());
            if (userFound != null && userFound.getEnabled()) {
                return new AdministrationUser(UserCode.fromValue(userFound.getId()),
                        UserEmail.fromValue(userFound.getEmail()));
            }
        }

        return null;
    }

}
