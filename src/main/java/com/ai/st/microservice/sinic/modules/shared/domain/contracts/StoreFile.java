package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

public interface StoreFile {

    String storeFilePermanently(byte[] bytes, String extensionFile, String namespace);

    String storeFileTemporarily(byte[] bytes, String extensionFile);

    void deleteFile(String pathFile);

}
