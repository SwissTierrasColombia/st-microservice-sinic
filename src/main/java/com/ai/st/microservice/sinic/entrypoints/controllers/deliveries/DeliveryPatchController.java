package com.ai.st.microservice.sinic.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.deliveries.application.send_delivery_to_cadastral_authority.DeliveryToCadastralAuthoritySender;
import com.ai.st.microservice.sinic.modules.deliveries.application.send_delivery_to_cadastral_authority.DeliveryToCadastralAuthoritySenderCommand;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryPatchController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPatchController.class);

    private final DeliveryToCadastralAuthoritySender cadastralAuthoritySender;

    public DeliveryPatchController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness, DeliveryToCadastralAuthoritySender cadastralAuthoritySender) {
        super(administrationBusiness, managerBusiness);
        this.cadastralAuthoritySender = cadastralAuthoritySender;
    }

    @PatchMapping(value = "api/sinic/v1/deliveries/{deliveryId}/status/delivered", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to delivered")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Status changed to sent to cadastral authority"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> changeStatusToSentToCadastalAuthority(
            @PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para enviar la entrega.");
            }

            validateDeliveryId(deliveryId);

            cadastralAuthoritySender.handle(
                    new DeliveryToCadastralAuthoritySenderCommand(
                            deliveryId, session.entityCode()
                    ));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToSentToCadastalAuthority#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToSentToCadastalAuthority#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToSentToCadastalAuthority#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new InputValidationException("La entrega no es válida.");
        }
    }

}