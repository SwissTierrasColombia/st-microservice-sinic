package com.ai.st.microservice.sinic.modules.files.application.notify_file_status;

import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.AdministrationUser;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.AdministrationMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.EmailTemplate;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.NotifierMessageBroker;

import java.util.HashMap;
import java.util.Map;

@Service
public class FileStatusNotifier implements CommandUseCase<FileStatusNotifierCommand> {

    private final static String FILE_TEMPLATE_ACCEPTED = "xtf_accepted";
    private final static String FILE_TEMPLATE_REJECTED = "xtf_rejected";

    private final static String SUBJECT_ACCEPTED = "Reporte de información catastral aceptado";
    private final static String SUBJECT_REJECTED = "Reporte de información catastral rechazado";

    private final AdministrationMicroservice administrationMicroservice;
    private final EmailTemplate template;
    private final NotifierMessageBroker notifierMessageBroker;

    public FileStatusNotifier(AdministrationMicroservice administrationMicroservice, EmailTemplate template,
                              NotifierMessageBroker notifierMessageBroker) {
        this.administrationMicroservice = administrationMicroservice;
        this.template = template;
        this.notifierMessageBroker = notifierMessageBroker;
    }

    @Override
    public void handle(FileStatusNotifierCommand command) {
        sendNotification(command);
    }

    private void sendNotification(FileStatusNotifierCommand command) {
        AdministrationUser userInformation = findUser(command.userCode());
        String template = createTemplate(command);

        String subject =
                command.status().equals(FileStatusNotifierCommand.StatusFile.ACCEPTED) ? SUBJECT_ACCEPTED : SUBJECT_REJECTED;

        notifierMessageBroker.sendNotification(subject, template, userInformation.email().value(), command.userCode());
    }

    private String createTemplate(FileStatusNotifierCommand command) {

        String templateFile =
                command.status().equals(FileStatusNotifierCommand.StatusFile.ACCEPTED) ? FILE_TEMPLATE_ACCEPTED : FILE_TEMPLATE_REJECTED;

        Map<String, Object> data = new HashMap<>();
        data.put("department", command.department());
        data.put("municipality", command.municipality());

        return template.parse(templateFile, data);
    }

    private AdministrationUser findUser(Long userCode) {
        return administrationMicroservice.findAdministrationUser(UserCode.fromValue(userCode));
    }

}
