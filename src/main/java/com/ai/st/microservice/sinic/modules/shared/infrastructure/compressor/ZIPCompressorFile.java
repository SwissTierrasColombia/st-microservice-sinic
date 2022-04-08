package com.ai.st.microservice.sinic.modules.shared.infrastructure.compressor;

import com.ai.st.microservice.sinic.modules.shared.domain.contracts.CompressorFile;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.CompressError;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.tracing.SCMTracing;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Service
public final class ZIPCompressorFile implements CompressorFile {

    private final Logger log = LoggerFactory.getLogger(ZIPCompressorFile.class);

    @Override
    public int countEntries(String filePath) throws CompressError {
        int count = 0;
        try {
            ZipFile zipFile = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                entries.nextElement();
                count++;
            }
            zipFile.close();
        } catch (IOException e) {
            String messageError = String
                    .format("Error leyendo el archivo zip para obtener la cantidad de archivos : %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new CompressError("Ha ocurrido un error leyendo el archivo zip.");
        }
        return count;
    }

    @Override
    public boolean checkIfFileIsPresent(String filePath, String extension) throws CompressError {
        try {
            ZipFile zipFile = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (FilenameUtils.getExtension(entry.getName()).equalsIgnoreCase(extension)) {
                    return true;
                }
            }
            zipFile.close();
        } catch (IOException e) {
            String messageError = String
                    .format("Error leyendo el archivo zip para verificar si contiene un archivo : %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new CompressError("Ha ocurrido un error leyendo el archivo zip.");
        }
        return false;
    }

    @Override
    public String compress(File file, String namespace, String zipName) throws CompressError {

        try {

            String path = namespace + File.separatorChar + zipName + ".zip";

            new File(namespace).mkdirs();
            File fileZip = new File(path);
            if (fileZip.exists()) {
                boolean fileRemovedSuccessful = fileZip.delete();
                if (!fileRemovedSuccessful) {
                    throw new CompressError("Ha ocurrido un error eliminando el archivo zip existente.");
                }
            }

            byte[] buffer = new byte[1024];

            FileOutputStream fileOutputStream = new FileOutputStream(fileZip);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            ZipEntry entry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(entry);

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            fileInputStream.close();

            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();

            return path;

        } catch (Exception e) {
            String messageError = String.format("Error comprimiendo y creando el archivo zip : %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new CompressError("Ha ocurrido un error creando el archivo zip.");
        }
    }

}
