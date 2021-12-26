package com.ai.st.microservice.sinic.modules.shared.domain.contracts;

import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.CompressError;

import java.io.File;

public interface CompressorFile {

    int countEntries(String filePath) throws CompressError;

    boolean checkIfFileIsPresent(String filePath, String extension) throws CompressError;

    String compress(File file, String namespace, String zipName) throws CompressError;

}
