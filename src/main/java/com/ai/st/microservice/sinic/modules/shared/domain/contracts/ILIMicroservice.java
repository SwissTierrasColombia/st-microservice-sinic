package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import com.ai.st.microservice.sinic.modules.files.domain.FileUUID;

public interface ILIMicroservice {

    void sendToValidation(FileUUID fileUUID, String pathFile, boolean skipGeometryValidation, boolean skipErrors);

}
