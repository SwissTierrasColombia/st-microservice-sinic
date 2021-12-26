package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;

import java.util.List;

public interface FileRepository {

    List<File> findByDeliveryId(DeliveryId deliveryId);

    File findBy(FileUUID fileUUID);

    void save(File file);

    void updateFileStatus(FileUUID uuid, FileStatus status, FileLog log);

    File search(FileId fileId);

    void remove(FileId fileId);

}
