package com.ai.st.microservice.sinic.modules.files.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class FileDoesNotBelongToDelivery extends DomainError {

    public FileDoesNotBelongToDelivery(Long fileId, Long deliveryId) {
        super("file_does_not_belong_to_delivery", String.format("El archivo '%d' no pertenece a la entrega '%d'.", fileId, deliveryId));
    }
}
