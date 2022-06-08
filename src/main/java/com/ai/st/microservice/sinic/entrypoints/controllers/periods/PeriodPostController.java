package com.ai.st.microservice.sinic.entrypoints.controllers.periods;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.entrypoints.controllers.periods.dto.AddPeriodsToCycleRequest;
import com.ai.st.microservice.sinic.entrypoints.controllers.periods.dto.PeriodRequest;
import com.ai.st.microservice.sinic.modules.cycles.application.add_periods.PeriodAggregator;
import com.ai.st.microservice.sinic.modules.cycles.application.add_periods.PeriodAggregatorCommand;
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

import java.util.List;

@Api(value = "Manage Periods", tags = { "Periods" })
@RestController
public final class PeriodPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(PeriodPostController.class);

    private final PeriodAggregator periodAggregator;

    public PeriodPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            PeriodAggregator periodAggregator) {
        super(administrationBusiness, managerBusiness);
        this.periodAggregator = periodAggregator;
    }

    @PostMapping(value = "api/sinic/v1/cycles/{cycleId}/periods", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add periods to cycle")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Periods added"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> addPeriodsToCycle(@PathVariable String cycleId,
            @RequestBody AddPeriodsToCycleRequest request, @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("addPeriodsToCycle");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            validatePeriods(request.getPeriods());

            periodAggregator.handle(PeriodAggregatorCommand.from(cycleId, request));

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error PeriodPostController@addPeriodsToCycle#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error PeriodPostController@addPeriodsToCycle#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error PeriodPostController@addPeriodsToCycle#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validatePeriods(List<PeriodRequest> periods) throws InputValidationException {
        if (periods.isEmpty()) {
            throw new InputValidationException("Se debe agregar al menos un período al ciclo.");
        }

        for (PeriodRequest periodRequest : periods) {
            if (periodRequest.getGroups().isEmpty()) {
                throw new InputValidationException("Se debe al menos un grupo a cada período.");
            }
        }
    }

}
