package com.ai.st.microservice.sinic.modules.files.application.get_file_url;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.files.domain.File;
import com.ai.st.microservice.sinic.modules.files.domain.FileId;
import com.ai.st.microservice.sinic.modules.files.domain.FileRepository;
import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileDoesNotBelongToDelivery;
import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileNotFound;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;
import com.ai.st.microservice.sinic.modules.shared.application.StringResponse;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class FileURLGetter implements QueryUseCase<FileURLGetterQuery, StringResponse> {

    private final DeliveryRepository deliveryRepository;
    private final FileRepository fileRepository;

    public FileURLGetter(DeliveryRepository deliveryRepository, FileRepository fileRepository) {
        this.deliveryRepository = deliveryRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public StringResponse handle(FileURLGetterQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        FileId fileId = new FileId(query.fileId());

        File file = verifyPermissions(deliveryId, fileId, query.role(), query.managerCode());

        return new StringResponse(file.url().value());
    }

    private File verifyPermissions(DeliveryId deliveryId, FileId fileId, Roles role, Long managerCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound(deliveryId.value());
        }

        if (role.equals(Roles.MANAGER)) {
            // verify status of the delivery
            if (!delivery.deliveryBelongToManager(ManagerCode.fromValue(managerCode)) || !delivery.isAvailableToManager()) {
                throw new UnauthorizedToSearchDelivery();
            }
        }

        File file = fileRepository.search(fileId);
        if (file == null) {
            throw new FileNotFound(fileId.value());
        }

        // verify attachment belong to delivery product
        if (!file.deliveryId().value().equals(deliveryId.value())) {
            throw new FileDoesNotBelongToDelivery(fileId.value(), deliveryId.value());
        }

        return file;
    }

}
