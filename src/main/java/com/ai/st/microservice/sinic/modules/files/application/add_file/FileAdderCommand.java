package com.ai.st.microservice.sinic.modules.files.application.add_file;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class FileAdderCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;
    private final String observations;
    private final byte[] bytes;
    private final String extension;

    public FileAdderCommand(Long deliveryId, Long managerCode, String observations, byte[] bytes, String extension) {
        this.deliveryId = deliveryId;
        this.managerCode = managerCode;
        this.observations = observations;
        this.bytes = bytes;
        this.extension = extension;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long managerCode() {
        return managerCode;
    }

    public String observations() {
        return observations;
    }

    public byte[] bytes() {
        return bytes;
    }

    public String extension() {
        return extension;
    }

}
