package com.ai.st.microservice.sinic.modules.deliveries.application.create_delivery;

import com.ai.st.microservice.sinic.modules.deliveries.domain.*;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.*;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.DateTime;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.ManagerMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.WorkspaceMicroservice;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.ManagerDoesNotBelongToMunicipality;
import org.apache.commons.lang.RandomStringUtils;

@Service
public final class DeliveryCreator implements CommandUseCase<CreateDeliveryCommand> {

    private final DeliveryRepository deliveryRepository;
    private final WorkspaceMicroservice workspaceMicroservice;
    private final ManagerMicroservice managerMicroservice;
    private final DateTime dateTime;

    public DeliveryCreator(DeliveryRepository deliveryRepository, WorkspaceMicroservice workspaceMicroservice,
                           ManagerMicroservice managerMicroservice, DateTime dateTime) {
        this.deliveryRepository = deliveryRepository;
        this.workspaceMicroservice = workspaceMicroservice;
        this.managerMicroservice = managerMicroservice;
        this.dateTime = dateTime;
    }

    @Override
    public void handle(CreateDeliveryCommand command) {

        MunicipalityCode municipalityCode = MunicipalityCode.fromValue(command.municipalityCode());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());
        UserCode userCode = UserCode.fromValue(command.userCode());

        verifyManagerBelongsToMunicipality(managerCode, municipalityCode);

        Delivery delivery = Delivery.create(
                DeliveryCode.fromValue(generateDeliveryCode()),
                defineManager(managerCode),
                defineLocality(municipalityCode),
                DeliveryObservations.fromValue(command.observations()),
                userCode,
                dateTime,
                DeliveryType.fromValue(command.type().name())
        );

        deliveryRepository.save(delivery);
    }

    private void verifyManagerBelongsToMunicipality(ManagerCode managerCode, MunicipalityCode municipalityCode) {
        boolean belong = workspaceMicroservice.managerBelongToMunicipality(managerCode, municipalityCode);
        if (!belong) {
            throw new ManagerDoesNotBelongToMunicipality(managerCode.value(), municipalityCode.value());
        }
    }

    private DeliveryManager defineManager(ManagerCode managerCode) {

        ManagerName managerName = managerMicroservice.getManagerName(managerCode);

        return DeliveryManager.builder()
                .code(managerCode)
                .name(managerName)
                .build();
    }

    private DeliveryLocality defineLocality(MunicipalityCode municipalityCode) {

        DepartmentMunicipality departmentMunicipality = workspaceMicroservice.getDepartmentMunicipalityName(municipalityCode);

        return DeliveryLocality.builder()
                .code(municipalityCode)
                .municipality(departmentMunicipality.municipality())
                .department(departmentMunicipality.department())
                .build();
    }

    private String generateDeliveryCode() {
        return RandomStringUtils.random(6, false, true);
    }


}
