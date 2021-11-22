package com.ai.st.microservice.sinic.entrypoints.controllers.files;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.deliveries.application.create_delivery.CreateDeliveryCommand;
import com.ai.st.microservice.sinic.modules.files.application.add_file.FileAdder;
import com.ai.st.microservice.sinic.modules.files.application.add_file.FileAdderCommand;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.CompressorFile;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.StoreFile;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Api(value = "Manage Files", tags = {"Files"})
@RestController
public final class FilePostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(FilePostController.class);

    private final FileAdder fileAdder;
    private final StoreFile storeFile;
    private final CompressorFile compressorFile;

    public FilePostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness, FileAdder fileAdder, StoreFile storeFile, CompressorFile compressorFile) {
        super(administrationBusiness, managerBusiness);
        this.fileAdder = fileAdder;
        this.storeFile = storeFile;
        this.compressorFile = compressorFile;
    }

    @PostMapping(value = "api/sinic/v1/deliveries", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Delivery created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> addFileToDelivery(
            @RequestBody AddFileToDelivery request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para crear la entrega.");
            }

            validateObservations(request.getObservations());
            validateXTFFile(request);

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error FilePostController@addFileToDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error FilePostController@addFileToDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error FilePostController@addFileToDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateObservations(String observations) throws InputValidationException {
        if (observations == null || observations.isEmpty()) {
            throw new InputValidationException("Las observaciones del archivo son obligatorias.");
        }
    }

    private FileAdderCommand validateXTFFile(AddFileToDelivery request)
            throws InputValidationException, IOException {

        MultipartFile file = request.getAttachment();
        if (file == null) {
            throw new InputValidationException("El archivo XTF es requerido.");
        }
        String extension = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
        boolean isZip = extension.equalsIgnoreCase("zip");
        if (!isZip) {
            throw new InputValidationException("El archivo debe cargarse en formato zip");
        }

        String temporalFilePath = storeFile.storeFileTemporarily(file.getBytes(), extension);
        int countEntries = compressorFile.countEntries(temporalFilePath);
        if (countEntries != 1) {
            throw new InputValidationException("El comprimido sólo debe contener un archivo y en formato XTF.");
        }

        boolean filePresent = compressorFile.checkIfFileIsPresent(temporalFilePath, "xtf");
        if (!filePresent) {
            throw new InputValidationException("El comprimido no contiene un archivo en formato XTF.");
        }

        storeFile.deleteFile(temporalFilePath);

        if (!request.getVersion().equalsIgnoreCase("1.0") && !request.getVersion().equalsIgnoreCase("1.1")) {
            throw new InputValidationException("La versión del submodelo de levantamiento debe ser 1.0 o 1.1");
        }

        return new AttachmentAssignerCommand.XTFAttachment(
                observations, file.getBytes(), extension, request.getVersion());
    }

}

@ApiModel(value = "AddFileToDelivery")
final class AddFileToDelivery {

    @ApiModelProperty(required = true, notes = "Attachment")
    private String observations;

    @ApiModelProperty(required = true, notes = "Attachment")
    private MultipartFile attachment;

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}