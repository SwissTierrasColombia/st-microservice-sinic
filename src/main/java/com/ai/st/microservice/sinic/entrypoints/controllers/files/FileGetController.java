package com.ai.st.microservice.sinic.entrypoints.controllers.files;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.files.application.FileResponse;
import com.ai.st.microservice.sinic.modules.files.application.find_files.FilesFinder;
import com.ai.st.microservice.sinic.modules.files.application.find_files.FilesFinderQuery;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class FileGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(FileGetController.class);

    private final ServletContext servletContext;
    private final FilesFinder filesFinder;

    public FileGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                             ServletContext servletContext, FilesFinder filesFinder) {
        super(administrationBusiness, managerBusiness);
        this.servletContext = servletContext;
        this.filesFinder = filesFinder;
    }

    @GetMapping(value = "api/sinic/v1/deliveries/{deliveryId}/files", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get files from delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Files got", response = FileResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> findAttachmentsFromDelivery(
            @PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (!session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para consultar los archivos de la entrega.");
            }

            validateDeliveryId(deliveryId);

            responseDto = filesFinder.handle(
                    new FilesFinderQuery(
                            deliveryId, session.role(), session.entityCode()
                    )
            ).list();

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error FileGetController@findAttachmentsFromDelivery#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error FileGetController@findAttachmentsFromDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

//    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments/{attachmentId}/download")
//    @ApiOperation(value = "Download file")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "File downloaded"),
//            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
//    @ResponseBody
//    public ResponseEntity<?> downloadAttachment(@PathVariable Long deliveryId,
//                                                @PathVariable Long deliveryProductId,
//                                                @PathVariable Long attachmentId,
//                                                @RequestHeader("authorization") String headerAuthorization) {
//
//        MediaType mediaType;
//        File file;
//        InputStreamResource resource;
//
//        try {
//
//            InformationSession session = this.getInformationSession(headerAuthorization);
//
//            validateDeliveryId(deliveryId);
//            validateDeliveryProductId(deliveryProductId);
//            validateFileId(attachmentId);
//
//            String pathFile = attachmentURLGetter.handle(new AttachmentURLGetterQuery(
//                    deliveryId, deliveryProductId, attachmentId, session.role(), session.entityCode()
//            )).value();
//
//            Path path = Paths.get(pathFile);
//            String fileName = path.getFileName().toString();
//
//            String mineType = servletContext.getMimeType(fileName);
//
//            try {
//                mediaType = MediaType.parseMediaType(mineType);
//            } catch (Exception e) {
//                mediaType = MediaType.APPLICATION_OCTET_STREAM;
//            }
//
//            file = new File(pathFile);
//            resource = new InputStreamResource(new FileInputStream(file));
//
//        } catch (DomainError e) {
//            log.error("Error FileGetController@downloadAttachment#Domain ---> " + e.getMessage());
//            return new ResponseEntity<>(new BasicResponseDto(e.errorMessage(), 2), HttpStatus.UNPROCESSABLE_ENTITY);
//        } catch (Exception e) {
//            log.error("Error FileGetController@downloadAttachment#General ---> " + e.getMessage());
//            return new ResponseEntity<>(new BasicResponseDto(e.getMessage(), 1), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return this.responseFile(file, mediaType, resource);
//    }


    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new InputValidationException("La entrega no es válida.");
        }
    }

    private void validateFileId(Long fileId) throws InputValidationException {
        if (fileId == null || fileId <= 0) {
            throw new InputValidationException("El identificador del archivo no válido.");
        }
    }

}
