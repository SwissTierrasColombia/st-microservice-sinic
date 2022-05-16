package com.ai.st.microservice.sinic.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.deliveries.application.remove_delivery.DeliveryRemover;
import com.ai.st.microservice.sinic.modules.deliveries.application.remove_delivery.DeliveryRemoverCommand;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.TracingKeyword;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryDeleteController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryDeleteController.class);

    private final DeliveryRemover deliveryRemover;

    public DeliveryDeleteController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            DeliveryRemover deliveryRemover) {
        super(administrationBusiness, managerBusiness);
        this.deliveryRemover = deliveryRemover;
    }

    @DeleteMapping(value = "api/sinic/v1/deliveries/{deliveryId}")
    @ApiOperation(value = "Remove delivery")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Delivery removed"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> removeDelivery(@PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("removeDelivery");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para eliminar la entrega.");
            }

            validateDeliveryId(deliveryId);

            deliveryRemover.handle(new DeliveryRemoverCommand(deliveryId, session.entityCode()));

            httpStatus = HttpStatus.NO_CONTENT;

        } catch (InputValidationException e) {
            log.error("Error DeliveryDeleteController@removeDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryDeleteController@removeDelivery#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryDeleteController@removeDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new InputValidationException("La entrega no es válida.");
        }
    }

}
