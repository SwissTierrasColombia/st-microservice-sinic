package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class UnsupportedToDownloadLog extends DomainError {

    public UnsupportedToDownloadLog(String errorMessage) {
        super("unsupported_to_download_log", errorMessage);
    }

}
