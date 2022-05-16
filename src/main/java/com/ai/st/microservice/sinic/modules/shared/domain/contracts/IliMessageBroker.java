package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import com.ai.st.microservice.sinic.modules.files.domain.FileUUID;
import com.ai.st.microservice.sinic.modules.files.domain.FileUrl;

public interface IliMessageBroker {

    void sendDataToIliProcess(FileUUID uuid, FileUrl url, String schema, int currentFile, int totalFiles);

}
