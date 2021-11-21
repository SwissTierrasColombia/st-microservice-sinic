package com.ai.st.microservice.sinic.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.WorkspaceFeignClient;
import com.ai.st.microservice.common.dto.workspaces.MicroserviceMunicipalityDto;
import com.ai.st.microservice.sinic.modules.shared.domain.*;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.WorkspaceMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.MunicipalityInvalid;
import org.springframework.stereotype.Service;

@Service
public final class HTTPWorkspaceMicroservice implements WorkspaceMicroservice {

    private final WorkspaceFeignClient workspaceFeignClient;

    public HTTPWorkspaceMicroservice(WorkspaceFeignClient workspaceFeignClient) {
        this.workspaceFeignClient = workspaceFeignClient;
    }

    @Override
    public DepartmentMunicipality getDepartmentMunicipalityName(MunicipalityCode municipalityCode) {

        MicroserviceMunicipalityDto municipalityDto = workspaceFeignClient.findMunicipalityByCode(municipalityCode.value());

        if (municipalityDto == null) {
            throw new MunicipalityInvalid(municipalityCode.value());
        }

        return DepartmentMunicipality.builder().department(DepartmentName.fromValue(municipalityDto.getDepartment().getName()))
                .municipality(MunicipalityName.fromValue(municipalityDto.getName())).build();
    }

    @Override
    public boolean managerBelongToMunicipality(ManagerCode managerCode, MunicipalityCode municipalityCode) {
        try {
            workspaceFeignClient.findWorskpaceByManagerAndMunicipality(managerCode.value(), municipalityCode.value());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
