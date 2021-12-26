package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import com.ai.st.microservice.sinic.modules.shared.domain.DepartmentMunicipality;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.MunicipalityCode;

public interface WorkspaceMicroservice {

    DepartmentMunicipality getDepartmentMunicipalityName(MunicipalityCode municipalityCode);

    boolean managerBelongToMunicipality(ManagerCode managerCode, MunicipalityCode municipalityCode);

}
