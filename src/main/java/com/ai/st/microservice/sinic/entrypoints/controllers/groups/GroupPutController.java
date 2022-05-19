package com.ai.st.microservice.sinic.entrypoints.controllers.groups;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.groups.application.update_group.GroupUpdater;
import com.ai.st.microservice.sinic.modules.groups.application.update_group.GroupUpdaterCommand;
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

@Api(value = "Manage Groups", tags = { "Groups" })
@RestController
public final class GroupPutController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(GroupPutController.class);

    private final GroupUpdater groupUpdater;

    public GroupPutController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            GroupUpdater groupUpdater) {
        super(administrationBusiness, managerBusiness);
        this.groupUpdater = groupUpdater;
    }

    @PutMapping(value = "api/sinic/v1/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update group")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Group updated"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> updateGroup(@PathVariable String groupId, @RequestBody CreateGroupRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("updateGroup");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            String groupName = request.getName();
            validateGroupName(groupName);

            groupUpdater.handle(new GroupUpdaterCommand(groupId, groupName));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error GroupPutController@updateGroup#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error GroupPutController@updateGroup#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error GroupPutController@updateGroup#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateGroupName(String name) throws InputValidationException {
        if (name == null || name.isEmpty()) {
            throw new InputValidationException("El nombre del grupo es requerido.");
        }
    }

}

@ApiModel(value = "UpdateGroupRequest")
final class UpdateGroupRequest {

    @ApiModelProperty(required = true, notes = "Group name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UpdateGroupRequest{" + "name='" + name + '\'' + '}';
    }
}
