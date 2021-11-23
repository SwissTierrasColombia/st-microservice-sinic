package com.ai.st.microservice.sinic.entrypoints.controllers.files;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.entrypoints.controllers.deliveries.DeliveryDeleteController;
import com.ai.st.microservice.sinic.modules.files.application.remove_file.FileRemover;
import com.ai.st.microservice.sinic.modules.files.application.remove_file.FileRemoverCommand;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Files", tags = {"Files"})
@RestController
public final class FileDeleteController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryDeleteController.class);

    private final FileRemover fileRemover;

    public FileDeleteController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness, FileRemover fileRemover) {
        super(administrationBusiness, managerBusiness);
        this.fileRemover = fileRemover;
    }

    @DeleteMapping(value = "api/sinic/v1/deliveries/{deliveryId}/files/{fileId}")
    @ApiOperation(value = "Remove file")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "File removed"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> removeFile(
            @PathVariable Long deliveryId,
            @PathVariable Long fileId,
            @RequestHeader("authorization") String headerAuthorization) {


        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para eliminar la entrega.");
            }

            validateDeliveryId(deliveryId);
            validateFileId(fileId);

            fileRemover.handle(
                    new FileRemoverCommand(
                            deliveryId, fileId, session.entityCode()
                    ));

            httpStatus = HttpStatus.NO_CONTENT;

        } catch (InputValidationException e) {
            log.error("Error FileDeleteController@removeFile#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (DomainError e) {
            log.error("Error FileDeleteController@removeFile#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error FileDeleteController@removeFile#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);

    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new InputValidationException("El identificador de la entrega no es válido.");
        }
    }

    private void validateFileId(Long fileId) throws InputValidationException {
        if (fileId == null || fileId <= 0) {
            throw new InputValidationException("El identificador del archivo no es válido.");
        }
    }

}
