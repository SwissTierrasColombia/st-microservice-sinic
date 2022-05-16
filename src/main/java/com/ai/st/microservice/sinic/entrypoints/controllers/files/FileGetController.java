package com.ai.st.microservice.sinic.entrypoints.controllers.files;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.sinic.entrypoints.controllers.ApiController;
import com.ai.st.microservice.sinic.modules.files.application.FileResponse;
import com.ai.st.microservice.sinic.modules.files.application.find_files.FilesFinder;
import com.ai.st.microservice.sinic.modules.files.application.find_files.FilesFinderQuery;
import com.ai.st.microservice.sinic.modules.files.application.get_file_url.FileURLGetter;
import com.ai.st.microservice.sinic.modules.files.application.get_file_url.FileURLGetterQuery;
import com.ai.st.microservice.sinic.modules.files.application.get_log_url.FileLogURLGetter;
import com.ai.st.microservice.sinic.modules.files.application.get_log_url.FileLogURLGetterQuery;
import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.TracingKeyword;
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

@Api(value = "Manage Files", tags = { "Files" })
@RestController
public final class FileGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(FileGetController.class);

    private final ServletContext servletContext;
    private final FilesFinder filesFinder;
    private final FileURLGetter fileURLGetter;
    private final FileLogURLGetter fileLogURLGetter;

    public FileGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            ServletContext servletContext, FilesFinder filesFinder, FileURLGetter fileURLGetter,
            FileLogURLGetter fileLogURLGetter) {
        super(administrationBusiness, managerBusiness);
        this.servletContext = servletContext;
        this.filesFinder = filesFinder;
        this.fileURLGetter = fileURLGetter;
        this.fileLogURLGetter = fileLogURLGetter;
    }

    @GetMapping(value = "api/sinic/v1/deliveries/{deliveryId}/files", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get files from delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Files got", response = FileResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> findAttachmentsFromDelivery(@PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            SCMTracing.setTransactionName("findAttachmentsFromDelivery");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (session.isManager() && !session.isSinic()) {
                throw new InputValidationException(
                        "El usuario no tiene permisos para consultar los archivos de la entrega.");
            }

            validateDeliveryId(deliveryId);

            responseDto = filesFinder.handle(new FilesFinderQuery(deliveryId, session.role(), session.entityCode()))
                    .list();

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error FileGetController@findAttachmentsFromDelivery#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error FileGetController@findAttachmentsFromDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping(value = "api/sinic/v1/deliveries/{deliveryId}/files/{fileId}/download")
    @ApiOperation(value = "Download file")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "File downloaded"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> downloadFile(@PathVariable Long deliveryId, @PathVariable Long fileId,
            @RequestHeader("authorization") String headerAuthorization) {

        MediaType mediaType;
        File file;
        InputStreamResource resource;

        try {

            SCMTracing.setTransactionName("downloadFile");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (session.isManager() && !session.isSinic()) {
                throw new InputValidationException(
                        "El usuario no tiene permisos para descargar archivos de la entrega.");
            }

            validateDeliveryId(deliveryId);
            validateFileId(fileId);

            String pathFile = fileURLGetter
                    .handle(new FileURLGetterQuery(deliveryId, fileId, session.role(), session.entityCode())).value();

            Path path = Paths.get(pathFile);
            String fileName = path.getFileName().toString();

            String mineType = servletContext.getMimeType(fileName);

            try {
                mediaType = MediaType.parseMediaType(mineType);
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            file = new File(pathFile);
            resource = new InputStreamResource(new FileInputStream(file));

        } catch (DomainError e) {
            SCMTracing.sendError(e.getMessage());
            log.error("Error FileGetController@downloadFile#Domain ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.errorMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            SCMTracing.sendError(e.getMessage());
            log.error("Error FileGetController@downloadFile#General ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseFile(file, mediaType, resource);
    }

    @GetMapping(value = "api/sinic/v1/deliveries/{deliveryId}/files/{fileId}/log/download")
    @ApiOperation(value = "Download file")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "File downloaded"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> downloadLog(@PathVariable Long deliveryId, @PathVariable Long fileId,
            @RequestHeader("authorization") String headerAuthorization) {

        MediaType mediaType;
        File file;
        InputStreamResource resource;

        try {

            SCMTracing.setTransactionName("downloadLog");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (session.isManager() && !session.isSinic()) {
                throw new InputValidationException("El usuario no tiene permisos para descargar logs de la entrega.");
            }

            validateDeliveryId(deliveryId);
            validateFileId(fileId);

            String pathFile = fileLogURLGetter
                    .handle(new FileLogURLGetterQuery(deliveryId, fileId, session.entityCode())).value();

            Path path = Paths.get(pathFile);
            String fileName = path.getFileName().toString();

            String mineType = servletContext.getMimeType(fileName);

            try {
                mediaType = MediaType.parseMediaType(mineType);
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            file = new File(pathFile);
            resource = new InputStreamResource(new FileInputStream(file));

        } catch (DomainError e) {
            SCMTracing.sendError(e.getMessage());
            log.error("Error FileGetController@downloadLog#Domain ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.errorMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            SCMTracing.sendError(e.getMessage());
            log.error("Error FileGetController@downloadLog#General ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseFile(file, mediaType, resource);
    }

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
