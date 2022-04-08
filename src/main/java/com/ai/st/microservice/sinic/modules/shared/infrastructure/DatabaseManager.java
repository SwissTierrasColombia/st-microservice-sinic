package com.ai.st.microservice.sinic.modules.shared.infrastructure;

import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.IDatabaseManager;
import com.ai.st.microservice.sinic.modules.shared.domain.exceptions.ErrorFromInfrastructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public final class DatabaseManager implements IDatabaseManager {

    private final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    @Value("${sinic.database.hostname}")
    private String databaseHost;

    @Value("${sinic.database.port}")
    private String databasePort;

    @Value("${sinic.database.username}")
    private String databaseUsername;

    @Value("${sinic.database.password}")
    private String databasePassword;

    @Value("${sinic.database.database}")
    private String databaseName;

    @Override
    public void createSchema(String schema) {

        try {

            String url = "jdbc:postgresql://" + databaseHost + ":" + databasePort + "/postgres";

            Connection connection = DriverManager.getConnection(url, databaseUsername, databasePassword);

            PreparedStatement stmt1 = connection
                    .prepareStatement(String.format("CREATE SCHEMA IF NOT EXISTS %s;", schema));
            stmt1.execute();

        } catch (SQLException e) {
            log.error(String.format("Error creando esquema %s : %s", schema, e.getMessage()));
            throw new ErrorFromInfrastructure(
                    "No se ha podido crear el esquema para realizar la importación de archivos XTF");
        }

    }

}
