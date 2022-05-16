package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import com.ai.st.microservice.sinic.modules.shared.domain.*;

public interface AdministrationMicroservice {

    AdministrationUser findAdministrationUser(UserCode userCode);

}
