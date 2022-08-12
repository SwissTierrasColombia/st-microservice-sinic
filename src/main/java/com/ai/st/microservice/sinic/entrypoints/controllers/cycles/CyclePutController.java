package com.ai.st.microservice.sinic.entrypoints.controllers.cycles;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.cycles.application.update_cycle.CycleUpdater;
import com.ai.st.microservice.sinic.modules.cycles.application.update_cycle.CycleUpdaterCommand;
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
public class CyclePutController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(CyclePutController.class);

    private final CycleUpdater cycleUpdater;

    public CyclePutController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            CycleUpdater cycleUpdater) {
        super(administrationBusiness, managerBusiness);
        this.cycleUpdater = cycleUpdater;
    }

    @PutMapping(value = "api/sinic/v1/cycles/{cycleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update cycle")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Cycle updated"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> updateCycle(@PathVariable String cycleId, @RequestBody UpdateCycleRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("updateCycle");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            int amountPeriods = request.getAmountPeriods();
            String observations = request.getObservations();
            validateAmountPeriods(amountPeriods);
            validateObservations(observations);

            cycleUpdater.handle(new CycleUpdaterCommand(cycleId, amountPeriods, observations, request.getStatus()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error CyclePutController@updateCycle#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error CyclePutController@updateCycle#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error CyclePutController@updateCycle#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
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

@ApiModel(value = "UpdateCycleRequest")
final class UpdateCycleRequest {

    @ApiModelProperty(required = true, notes = "Amount periods")
    private int amountPeriods;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    @ApiModelProperty(required = true, notes = "Status")
    private boolean status;

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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}