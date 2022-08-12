package com.ai.st.microservice.sinic.modules.deliveries.application.process_pending_deliveries;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.files.domain.File;
import com.ai.st.microservice.sinic.modules.files.domain.FileRepository;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.IDatabaseManager;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.IliMessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public final class PendingDeliveryExecutor implements CommandUseCase<PendingDeliveryExecutorCommand> {

    private final Logger logger = LoggerFactory.getLogger(PendingDeliveryExecutor.class);

    private final DeliveryRepository deliveryRepository;
    private final FileRepository fileRepository;
    private final IliMessageBroker messageBroker;

    public PendingDeliveryExecutor(DeliveryRepository deliveryRepository, FileRepository fileRepository,
                                   IliMessageBroker messageBroker) {
        this.deliveryRepository = deliveryRepository;
        this.fileRepository = fileRepository;
        this.messageBroker = messageBroker;
    }

    @Override
    public void handle(PendingDeliveryExecutorCommand command) {
        final var pendingDeliveries = getPendingDeliveries();

        System.out.println("# DELIVERIES ---> " + pendingDeliveries.size());

        pendingDeliveries.forEach(delivery -> sendFilesToQueue(delivery, filesByDelivery(delivery.id())));
    }

    private List<Delivery> getPendingDeliveries() {
        return deliveryRepository.findBy(List.of(DeliveryStatus.fromValue("SENT_CADASTRAL_AUTHORITY")));
    }

    private List<File> filesByDelivery(DeliveryId deliveryId) {
        return fileRepository.findByDeliveryId(deliveryId);
    }

    private void sendFilesToQueue(Delivery delivery, List<File> files) {

        String schemaName = String.format("sinic_%s_%s", delivery.code().value(), delivery.locality().code().value());

        logger.info(String.format("Esquema creado para la entrega %s", delivery.code().value()));

        int index = 1;
        for (File file : files) {
            messageBroker.sendDataToIliProcess(file.uuid(), file.url(), schemaName, index, files.size(),
                    isLarge(file.size().value()));
            index++;
        }
    }

    private boolean isLarge(long bytes) {
        long kilobytes = (bytes / 1024);
        long megabytes = (kilobytes / 1024);
        return megabytes > 200;
    }
}
