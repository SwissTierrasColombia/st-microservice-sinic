package com.ai.st.microservice.sinic.entrypoints.controllers.cycles;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.cycles.application.find_cycles.CycleFinder;
import com.ai.st.microservice.sinic.modules.cycles.application.find_cycles.CycleFinderQuery;
import com.ai.st.microservice.sinic.modules.cycles.application.search_cycle.CycleSearcher;
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
public final class CycleGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(CycleGetController.class);

    private final CycleFinder cycleFinder;
    private final CycleSearcher cycleSearcher;

    public CycleGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            CycleFinder cycleFinder, CycleSearcher cycleSearcher) {
        super(administrationBusiness, managerBusiness);
        this.cycleFinder = cycleFinder;
        this.cycleSearcher = cycleSearcher;
    }

    @GetMapping(value = "api/sinic/v1/cycles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find cycles")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Cycles found"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> findCycles(@RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("findCycles");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            responseDto = cycleFinder.handle(new CycleFinderQuery()).list();

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error CycleGetController@findCycles#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error CycleGetController@findCycles#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping(value = "api/sinic/v1/cycles/{cycleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find cycles")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Cycles found"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> searchCycle(@PathVariable String cycleId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("searchCycle");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            responseDto = cycleSearcher.handle(new CycleSearcherQuery(cycleId));

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error CycleGetController@searchCycle#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error CycleGetController@searchCycle#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
