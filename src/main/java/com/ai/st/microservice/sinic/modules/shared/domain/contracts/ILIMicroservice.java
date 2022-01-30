package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import com.ai.st.microservice.sinic.modules.files.domain.FileUUID;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;

public interface ILIMicroservice {

    void sendToValidation(FileUUID fileUUID, UserCode userCode, String pathFile, boolean skipGeometryValidation, boolean skipErrors);

}
