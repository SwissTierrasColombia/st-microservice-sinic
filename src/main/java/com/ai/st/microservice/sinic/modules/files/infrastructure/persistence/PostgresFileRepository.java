package com.ai.st.microservice.sinic.modules.files.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.files.domain.*;
import com.ai.st.microservice.sinic.modules.files.infrastructure.persistence.jpa.FileJPARepository;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.DeliveryEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.FileEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.FileStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PostgresFileRepository implements FileRepository {

    private final FileJPARepository fileJPARepository;

    public PostgresFileRepository(FileJPARepository fileJPARepository) {
        this.fileJPARepository = fileJPARepository;
    }

    @Override
    public List<File> findByDeliveryId(DeliveryId deliveryId) {
        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setId(deliveryId.value());
        return fileJPARepository.findByDelivery(deliveryEntity)
                .stream().map(this::mapping).collect(Collectors.toList());
    }

    @Override
    public void save(File file) {

        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setId(file.deliveryId().value());

        FileEntity fileEntity = new FileEntity();
        fileEntity.setUuid(file.uuid().value());
        fileEntity.setCreatedAt(file.date().value());
        fileEntity.setDateStatusAt(file.dateStatus().value());
        fileEntity.setValid(null);
        fileEntity.setLog(null);
        fileEntity.setObservations(file.observations().value());
        fileEntity.setStatus(mappingEnum(file.status()));
        fileEntity.setUrl(file.url().value());
        fileEntity.setUserCode(file.user().value());
        fileEntity.setVersion(file.version().value());
        fileEntity.setDelivery(deliveryEntity);

        fileJPARepository.save(fileEntity);
    }

    @Override
    public void updateFileStatus(FileUUID uuid, FileStatus status, FileLog log) {

        FileEntity fileEntity = fileJPARepository.findByUuid(uuid.value());

        FileStatusEnum statusEntity = mappingEnum(status);
        fileEntity.setStatus(statusEntity);

        if (status.value().equals(FileStatus.Status.SUCCESSFUL)) {
            fileEntity.setValid(true);
            fileEntity.setLog(null);
        } else if (status.value().equals(FileStatus.Status.UNSUCCESSFUL)) {
            fileEntity.setValid(false);
            fileEntity.setLog(log.value());
        }

        fileJPARepository.save(fileEntity);
    }

    private FileStatusEnum mappingEnum(FileStatus status) {
        switch (status.value()) {
            case SUCCESSFUL:
                return FileStatusEnum.SUCCESSFUL;
            case UNSUCCESSFUL:
                return FileStatusEnum.UNSUCCESSFUL;
            case IN_VALIDATION:
            default:
                return FileStatusEnum.IN_VALIDATION;
        }
    }

    private File mapping(FileEntity fileEntity) {
        return File.fromPrimitives(
                fileEntity.getId(),
                fileEntity.getUuid(),
                fileEntity.getCreatedAt(),
                fileEntity.getDateStatusAt(),
                fileEntity.getValid(),
                fileEntity.getObservations(),
                fileEntity.getStatus().name(),
                fileEntity.getUrl(),
                fileEntity.getVersion(),
                fileEntity.getLog(),
                fileEntity.getUserCode(),
                fileEntity.getDelivery().getId()
        );
    }

}
