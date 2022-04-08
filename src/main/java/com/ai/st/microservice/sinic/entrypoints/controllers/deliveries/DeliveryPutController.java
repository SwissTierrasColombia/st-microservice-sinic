package com.ai.st.microservice.sinic.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.deliveries.application.update_delivery.DeliveryUpdater;
import com.ai.st.microservice.sinic.modules.deliveries.application.update_delivery.DeliveryUpdaterCommand;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.TracingKeyword;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryPutController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPutController.class);

    private final DeliveryUpdater deliveryUpdater;

    public DeliveryPutController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            DeliveryUpdater deliveryUpdater) {
        super(administrationBusiness, managerBusiness);
        this.deliveryUpdater = deliveryUpdater;
    }

    @PutMapping(value = "api/sinic/v1/deliveries/{deliveryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update delivery")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Delivery updated"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> updateDelivery(@PathVariable Long deliveryId, @RequestBody UpdateDeliveryRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("updateDelivery");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para actualizar la entrega.");
            }

            validateDeliveryId(deliveryId);

            String observations = request.getObservations();
            validateObservations(observations);

            deliveryUpdater.handle(new DeliveryUpdaterCommand(deliveryId, observations, session.entityCode()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPutController@updateDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPutController@updateDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPutController@updateDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0)
            throw new InputValidationException("La entrega no es válida");
    }

    private void validateObservations(String observations) throws InputValidationException {
        if (observations == null || observations.isEmpty())
            throw new InputValidationException("Las observaciones son requeridas.");
    }

}

@ApiModel(value = "UpdateDeliveryRequest")
final class UpdateDeliveryRequest {

    @ApiModelProperty(notes = "Observations")
    private String observations;

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "UpdateDeliveryRequest{" + "observations='" + observations + '\'' + '}';
    }
}
