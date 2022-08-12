package com.ai.st.microservice.sinic.entrypoints.controllers.cycles;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.cycles.application.delete_cycle.CycleRemover;
import com.ai.st.microservice.sinic.modules.cycles.application.delete_cycle.CycleRemoverCommand;
import com.ai.st.microservice.sinic.modules.cycles.application.search_cycle.CycleSearcherQuery;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Cycles", tags = { "Cycles" })
@RestController
public final class CycleDeleteController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(CycleDeleteController.class);

    private final CycleRemover cycleRemover;

    public CycleDeleteController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            CycleRemover cycleRemover) {
        super(administrationBusiness, managerBusiness);
        this.cycleRemover = cycleRemover;
    }

    @DeleteMapping(value = "api/sinic/v1/cycles/{cycleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find cycles")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Cycles found"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> deleteCycleById(@PathVariable String cycleId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("deleteCycleById");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            cycleRemover.handle(new CycleRemoverCommand(cycleId));

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error CycleDeleteController@deleteCycleById#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error CycleDeleteController@deleteCycleById#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
