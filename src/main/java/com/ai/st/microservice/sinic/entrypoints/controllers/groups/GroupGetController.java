package com.ai.st.microservice.sinic.entrypoints.controllers.groups;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.groups.application.find_groups.GroupFinder;
import com.ai.st.microservice.sinic.modules.groups.application.find_groups.GroupFinderQuery;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Manage Groups", tags = { "Groups" })
@RestController
public final class GroupGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(GroupGetController.class);

    private final GroupFinder groupFinder;

    public GroupGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            GroupFinder groupFinder) {
        super(administrationBusiness, managerBusiness);
        this.groupFinder = groupFinder;
    }

    @GetMapping(value = "api/sinic/v1/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find groups")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Groups found"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> findGroups(@RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("findGroups");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            responseDto = groupFinder.handle(new GroupFinderQuery()).list();

            httpStatus = HttpStatus.CREATED;

        } catch (DomainError e) {
            log.error("Error GroupGetController@findGroups#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error GroupGetController@findGroups#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
