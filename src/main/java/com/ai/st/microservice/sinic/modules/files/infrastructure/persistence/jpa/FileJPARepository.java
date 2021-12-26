package com.ai.st.microservice.sinic.modules.files.infrastructure.persistence.jpa;

import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.DeliveryEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileJPARepository extends CrudRepository<FileEntity, Long> {

    List<FileEntity> findByDelivery(DeliveryEntity delivery);

    FileEntity findByUuid(String uuid);

}
