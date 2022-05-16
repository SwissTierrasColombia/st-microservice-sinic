package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import com.ai.st.microservice.sinic.modules.shared.domain.AdministrationUser;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerName;

public interface ManagerMicroservice {

    ManagerName getManagerName(ManagerCode managerCode);

    AdministrationUser findSinicUser(ManagerCode managerCode);

}
