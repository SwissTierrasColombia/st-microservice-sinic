package com.ai.st.microservice.sinic.entrypoints.controllers;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;
import com.ai.st.microservice.common.exceptions.DisconnectedMicroserviceException;

import com.ai.st.microservice.sinic.modules.shared.application.Roles;
import com.ai.st.microservice.sinic.modules.shared.application.SubRoles;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;

public abstract class ApiController {

    @Value("${crypto.token-igac}")
    private String tokenIGAC;

    protected final AdministrationBusiness administrationBusiness;
    protected final ManagerBusiness managerBusiness;

    public ApiController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness) {
        this.administrationBusiness = administrationBusiness;
        this.managerBusiness = managerBusiness;
    }

    protected MicroserviceUserDto getUserSession(String headerAuthorization)
            throws DisconnectedMicroserviceException {
        MicroserviceUserDto userDtoSession = administrationBusiness.getUserByToken(headerAuthorization);
        if (userDtoSession == null) {
            throw new DisconnectedMicroserviceException("Ha ocurrido un error consultando el usuario");
        }
        return userDtoSession;
    }

    protected InformationSession getInformationSession(String headerAuthorization) throws DisconnectedMicroserviceException {
        MicroserviceUserDto userDtoSession = this.getUserSession(headerAuthorization);
        if (administrationBusiness.isManager(userDtoSession)) {
            MicroserviceManagerDto managerDto = managerBusiness.getManagerByUserCode(userDtoSession.getId());
            SubRoles subRole = managerBusiness.userManagerIsSINIC(userDtoSession.getId()) ? SubRoles.SINIC_MANAGER : SubRoles.DIRECTOR;
            return new InformationSession(Roles.MANAGER, managerDto.getId(), userDtoSession.getId(), managerDto.getName(), subRole);
        } else if (administrationBusiness.isAdministrator(userDtoSession)) {
            return new InformationSession(Roles.CADASTRAL_AUTHORITY, userDtoSession.getId());
        }
        throw new RuntimeException("User information not found");
    }

    protected ResponseEntity<?> responseFile(File file, MediaType mediaType, InputStreamResource resource) {
        String extension = Files.getFileExtension(file.getName());
        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + file.getName())
                .contentType(mediaType).contentLength(file.length())
                .header("extension", extension)
                .header("filename", file.getName() + extension).body(resource);
    }

    public final static class InformationSession {

        private final Roles role;
        private final Long entityCode;
        private final Long userCode;
        private final String entityName;
        private final SubRoles subRole;

        public InformationSession(Roles role, Long entityCode, Long userCode, String entityName, SubRoles subRole) {
            this.role = role;
            this.entityCode = entityCode;
            this.userCode = userCode;
            this.entityName = entityName;
            this.subRole = subRole;
        }

        public InformationSession(Roles role, Long userCode) {
            this.entityCode = null;
            this.entityName = null;
            this.role = role;
            this.userCode = userCode;
            this.subRole = null;
        }

        public Roles role() {
            return role;
        }

        public Long entityCode() {
            return entityCode;
        }

        public Long userCode() {
            return userCode;
        }

        public String entityName() {
            return entityName;
        }

        public SubRoles subRole() {
            return subRole;
        }

        public boolean isSinic() {
            return this.subRole() != null && this.subRole().equals(SubRoles.SINIC_MANAGER);
        }

        public boolean isManager() {
            return this.role().equals(Roles.MANAGER);
        }

        public boolean isCadastalAuthority() {
            return this.role().equals(Roles.CADASTRAL_AUTHORITY);
        }

    }

    public boolean matchTokenIGAC(String token) {
        return tokenIGAC.equals(token);
    }

}
