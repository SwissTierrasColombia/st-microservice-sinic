package com.ai.st.microservice.sinic.modules.files.application.find_files;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.files.application.FileResponse;
import com.ai.st.microservice.sinic.modules.files.domain.FileRepository;
import com.ai.st.microservice.sinic.modules.shared.application.ListResponse;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class FilesFinder implements QueryUseCase<FilesFinderQuery, ListResponse<FileResponse>> {

    private final DeliveryRepository deliveryRepository;
    private final FileRepository fileRepository;

    public FilesFinder(DeliveryRepository deliveryRepository, FileRepository fileRepository) {
        this.deliveryRepository = deliveryRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public ListResponse<FileResponse> handle(FilesFinderQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());

        verifyPermissions(deliveryId, query.role(), query.managerCode());

        return new ListResponse<>(fileRepository.findByDeliveryId(deliveryId).stream().map(FileResponse::fromAggregate)
                .collect(Collectors.toList()));
    }

    private void verifyPermissions(DeliveryId deliveryId, Roles role, Long managerCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound(deliveryId.value());
        }

        if (role.equals(Roles.MANAGER)) {
            // verify status of the delivery
            if (!delivery.deliveryBelongToManager(ManagerCode.fromValue(managerCode))
                    || !delivery.isAvailableToManager()) {
                throw new UnauthorizedToSearchDelivery();
            }
        }

    }

}
