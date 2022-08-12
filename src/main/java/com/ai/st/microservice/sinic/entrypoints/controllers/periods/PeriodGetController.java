package com.ai.st.microservice.sinic.entrypoints.controllers.periods;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.cycles.application.find_periods.PeriodFinder;
import com.ai.st.microservice.sinic.modules.cycles.application.find_periods.PeriodFinderQuery;
import com.ai.st.microservice.sinic.modules.shared.application.PageableResponse;
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

@Api(value = "Manage Periods", tags = { "Periods" })
@RestController
public final class PeriodGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(PeriodGetController.class);

    private final PeriodFinder periodFinder;

    public PeriodGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            PeriodFinder periodFinder) {
        super(administrationBusiness, managerBusiness);
        this.periodFinder = periodFinder;
    }

    @GetMapping(value = "api/sinic/v1/cycles/{cycleId}/periods", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get periods by cycle")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Periods gotten", response = PageableResponse.class),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> findPeriodsByCycle(@PathVariable String cycleId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("findPeriodsByCycle");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            responseDto = periodFinder.handle(new PeriodFinderQuery(cycleId)).list();

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error PeriodGetController@findPeriodsByCycle#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error PeriodGetController@findPeriodsByCycle#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
