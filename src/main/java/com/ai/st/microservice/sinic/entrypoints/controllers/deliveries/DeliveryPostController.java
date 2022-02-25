package com.ai.st.microservice.sinic.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.deliveries.application.create_delivery.CreateDeliveryCommand;
import com.ai.st.microservice.sinic.modules.deliveries.application.create_delivery.DeliveryCreator;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPostController.class);

    private final DeliveryCreator deliveryCreator;

    public DeliveryPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                  DeliveryCreator deliveryCreator) {
        super(administrationBusiness, managerBusiness);
        this.deliveryCreator = deliveryCreator;
    }

    @PostMapping(value = "api/sinic/v1/deliveries", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Delivery created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> createDelivery(@RequestBody CreateDeliveryRequest request,
                                            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para crear la entrega.");
            }

            String municipalityCode = request.getMunicipalityCode();
            validateMunicipality(municipalityCode);

            String observations = request.getObservations();
            validateObservations(observations);

            deliveryCreator.handle(
                    new CreateDeliveryCommand(
                            municipalityCode,
                            session.entityCode(),
                            session.userCode(),
                            observations,
                            CreateDeliveryCommand.DeliveryType.valueOf(request.getType())));

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPostController@createDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryPostController@createDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryPostController@createDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateMunicipality(String municipalityCode) throws InputValidationException {
        if (municipalityCode == null || municipalityCode.isEmpty()) {
            throw new InputValidationException("El municipio es requerido.");
        }
    }

    private void validateObservations(String observations) throws InputValidationException {
        if (observations == null || observations.isEmpty()) {
            throw new InputValidationException("Las observaciones de la entrega son obligatorias.");
        }
    }

}

@ApiModel(value = "CreateDeliveryRequest")
final class CreateDeliveryRequest {

    @ApiModelProperty(required = true, notes = "Municipality ID")
    private String municipalityCode;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    @ApiModelProperty(required = true, notes = "Observations")
    private String type;

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}