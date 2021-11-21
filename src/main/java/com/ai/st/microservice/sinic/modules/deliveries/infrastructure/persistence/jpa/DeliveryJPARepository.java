package com.ai.st.microservice.sinic.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.DeliveryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DeliveryJPARepository extends PagingAndSortingRepository<DeliveryEntity, Long>,
        JpaSpecificationExecutor<DeliveryEntity> {

    Page<DeliveryEntity> findAll(Pageable pageable);

    List<DeliveryEntity> findAll(Sort sort);

    List<DeliveryEntity> findAll();

}
