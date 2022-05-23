package com.ai.st.microservice.sinic.entrypoints.controllers.cycles;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.cycles.application.create_cycle.CycleCreator;
import com.ai.st.microservice.sinic.modules.cycles.application.create_cycle.CycleCreatorCommand;
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

@Api(value = "Manage Cycles", tags = { "Cycles" })
@RestController
public final class CyclePostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(CyclePostController.class);

    private final CycleCreator cycleCreator;

    public CyclePostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            CycleCreator cycleCreator) {
        super(administrationBusiness, managerBusiness);
        this.cycleCreator = cycleCreator;
    }

    @PostMapping(value = "api/sinic/v1/cycles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create cycle")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Cycle created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> createCycle(@RequestBody CreateCycleRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("createCycle");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            int year = request.getYear();
            int amountPeriods = request.getAmountPeriods();
            String observations = request.getObservations();
            validateYear(year);
            validateAmountPeriods(amountPeriods);
            validateObservations(observations);

            cycleCreator.handle(new CycleCreatorCommand(year, observations, amountPeriods));

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error CyclePostController@createCycle#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error CyclePostController@createCycle#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error CyclePostController@createCycle#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateYear(Integer year) throws InputValidationException {
        if (year <= 999) {
            throw new InputValidationException("El año del ciclo es inválido.");
        }
    }

    private void validateAmountPeriods(Integer amount) throws InputValidationException {
        if (amount <= 0) {
            throw new InputValidationException("Un ciclo debe tener al menos 1 período.");
        }
    }

    private void validateObservations(String observations) throws InputValidationException {
        if (observations == null || observations.isEmpty()) {
            throw new InputValidationException("Las observaciones son requeridas.");
        }
    }

}

@ApiModel(value = "CreateCycleRequest")
final class CreateCycleRequest {

    @ApiModelProperty(required = true, notes = "Year")
    private int year;

    @ApiModelProperty(required = true, notes = "Amount periods")
    private int amountPeriods;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAmountPeriods() {
        return amountPeriods;
    }

    public void setAmountPeriods(int amountPeriods) {
        this.amountPeriods = amountPeriods;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "CreateCycleRequest{" + "year=" + year + ", amountPeriods=" + amountPeriods + ", observations='"
                + observations + '\'' + '}';
    }
}