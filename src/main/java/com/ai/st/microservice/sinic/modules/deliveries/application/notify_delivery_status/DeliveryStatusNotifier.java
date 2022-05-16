package com.ai.st.microservice.sinic.modules.deliveries.application.notify_delivery_status;

import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.AdministrationUser;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.EmailTemplate;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.ManagerMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.NotifierMessageBroker;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeliveryStatusNotifier implements CommandUseCase<DeliveryStatusNotifierCommand> {

    private final static String FILE_TEMPLATE_IMPORT_SUCCESSFUL = "delivery_imported";

    private final static String SUBJECT_IMPORT_SUCCESSFUL = "Reporte de información catastral cargado exitosamente";

    private final ManagerMicroservice managerMicroservice;
    private final EmailTemplate template;
    private final NotifierMessageBroker notifierMessageBroker;

    public DeliveryStatusNotifier(ManagerMicroservice managerMicroservice, EmailTemplate template,
            NotifierMessageBroker notifierMessageBroker) {
        this.managerMicroservice = managerMicroservice;
        this.template = template;
        this.notifierMessageBroker = notifierMessageBroker;
    }

    @Override
    public void handle(DeliveryStatusNotifierCommand command) {
        sendNotification(command);
    }

    private void sendNotification(DeliveryStatusNotifierCommand command) {
        AdministrationUser userInformation = findUser(command.managerCode());

        String template = createTemplate(command);

        String subject = SUBJECT_IMPORT_SUCCESSFUL;
        if (command.status().equals(DeliveryStatusNotifierCommand.StatusDelivery.IMPORT_SUCCESSFUL)) {
            subject = SUBJECT_IMPORT_SUCCESSFUL;
        }

        notifierMessageBroker.sendNotification(subject, template, userInformation.email().value(),
                userInformation.code().value());
    }

    private String createTemplate(DeliveryStatusNotifierCommand command) {

        String templateFile = FILE_TEMPLATE_IMPORT_SUCCESSFUL;
        if (command.status().equals(DeliveryStatusNotifierCommand.StatusDelivery.IMPORT_SUCCESSFUL)) {
            templateFile = FILE_TEMPLATE_IMPORT_SUCCESSFUL;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("department", command.department());
        data.put("municipality", command.municipality());

        return template.parse(templateFile, data);
    }

    private AdministrationUser findUser(Long managerCode) {
        return managerMicroservice.findSinicUser(ManagerCode.fromValue(managerCode));
    }

}
