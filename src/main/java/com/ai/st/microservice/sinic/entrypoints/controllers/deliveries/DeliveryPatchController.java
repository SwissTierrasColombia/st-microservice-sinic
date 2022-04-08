package com.ai.st.microservice.sinic.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.deliveries.application.public_change_status.DeliveryPublicStatusChanger;
import com.ai.st.microservice.sinic.modules.deliveries.application.public_change_status.DeliveryPublicStatusChangerCommand;
import com.ai.st.microservice.sinic.modules.deliveries.application.send_delivery_to_cadastral_authority.DeliveryToCadastralAuthoritySender;
import com.ai.st.microservice.sinic.modules.deliveries.application.send_delivery_to_cadastral_authority.DeliveryToCadastralAuthoritySenderCommand;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.crypto.CryptoService;
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
public final class DeliveryPatchController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPatchController.class);

    private final DeliveryToCadastralAuthoritySender cadastralAuthoritySender;
    private final DeliveryPublicStatusChanger deliveryPublicStatusChanger;
    private final CryptoService crypto;

    public DeliveryPatchController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            DeliveryToCadastralAuthoritySender cadastralAuthoritySender,
            DeliveryPublicStatusChanger deliveryPublicStatusChanger, CryptoService crypto) {
        super(administrationBusiness, managerBusiness);
        this.cadastralAuthoritySender = cadastralAuthoritySender;
        this.deliveryPublicStatusChanger = deliveryPublicStatusChanger;
        this.crypto = crypto;
    }

    @PatchMapping(value = "api/sinic/v1/deliveries/{deliveryId}/status/delivered", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to delivered")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to sent to cadastral authority"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeStatusToSentToCadastalAuthority(@PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeStatusToSentToCadastalAuthority");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para enviar la entrega.");
            }

            validateDeliveryId(deliveryId);

            cadastralAuthoritySender
                    .handle(new DeliveryToCadastralAuthoritySenderCommand(deliveryId, session.entityCode()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToSentToCadastalAuthority#Validation ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToSentToCadastalAuthority#Domain ---> "
                    + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToSentToCadastalAuthority#General ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PatchMapping(value = "api/sinic/v1/deliveries/{deliveryId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to delivered")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeStatusToDelivery(@PathVariable Long deliveryId,
            @RequestBody ChangeDeliveryStatusRequest request,
            @RequestHeader("st-token") String stPublicTokenEncrypted) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeStatusToDelivery");
            SCMTracing.addCustomParameter(TracingKeyword.ST_TOKEN, stPublicTokenEncrypted);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            String token = crypto.decrypt(stPublicTokenEncrypted);
            if (!matchTokenIGAC(token)) {
                throw new InputValidationException(
                        "El usuario no tiene permisos para actualizar el estado de la entrega.");
            }

            validateDeliveryId(deliveryId);
            validateStatus(request.getStatus());

            log.info(String.format("Request to change status to %s for delivery %s", request.getStatus(), deliveryId));

            deliveryPublicStatusChanger.handle(new DeliveryPublicStatusChangerCommand(deliveryId,
                    DeliveryPublicStatusChangerCommand.Status.valueOf(request.getStatus())));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToDelivery#General ---> " + e.getMessage());
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

    private void validateStatus(String status) throws InputValidationException {
        try {
            DeliveryPublicStatusChangerCommand.Status.valueOf(status);
        } catch (Exception e) {
            throw new InputValidationException("El estado no es válido.");
        }
    }

}

@ApiModel(value = "ChangeDeliveryStatusRequest")
final class ChangeDeliveryStatusRequest {

    @ApiModelProperty(required = true, notes = "Observations")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChangeDeliveryStatusRequest{" + "status='" + status + '\'' + '}';
    }
}
