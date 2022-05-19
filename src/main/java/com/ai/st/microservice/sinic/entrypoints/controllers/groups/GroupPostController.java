package com.ai.st.microservice.sinic.entrypoints.controllers.groups;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.groups.application.create_group.GroupCreator;
import com.ai.st.microservice.sinic.modules.groups.application.create_group.GroupCreatorCommand;
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
public final class GroupPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(GroupPostController.class);

    private final GroupCreator groupCreator;

    public GroupPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            GroupCreator groupCreator) {
        super(administrationBusiness, managerBusiness);
        this.groupCreator = groupCreator;
    }

    @PostMapping(value = "api/sinic/v1/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create group")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Group created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("createGroup");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            String groupName = request.getName();
            validateGroupName(groupName);

            groupCreator.handle(new GroupCreatorCommand(groupName));

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error GroupPostController@createGroup#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error GroupPostController@createGroup#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error GroupPostController@createGroup#General ---> " + e.getMessage());
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

@ApiModel(value = "CreateGroupRequest")
final class CreateGroupRequest {

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
        return "CreateGroupRequest{" + "name='" + name + '\'' + '}';
    }
}