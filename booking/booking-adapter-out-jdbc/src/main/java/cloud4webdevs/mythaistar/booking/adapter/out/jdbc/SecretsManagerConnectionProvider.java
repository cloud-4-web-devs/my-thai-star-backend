package cloud4webdevs.mythaistar.booking.adapter.out.jdbc;

import com.amazonaws.secretsmanager.caching.SecretCache;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class SecretsManagerConnectionProvider implements ConnectionProvider {
    private final Connection connection;

    public SecretsManagerConnectionProvider(String dbSecretId) {
        Objects.requireNonNull(dbSecretId, "Database secret id cannot be null!");
        final var secrets = new SecretCache();
        final var dbSecretAsJson = secrets.getSecretString(dbSecretId);
        Objects.requireNonNull(dbSecretId, "Database secret cannot be null!");
        try {
            final var dbConnectionProps = JSON.std.mapFrom(dbSecretAsJson);
            final var dbUrl = getDbUrlFrom(dbConnectionProps);
            final var username = getUsernameFrom(dbConnectionProps);
            final var password = getPasswordFrom(dbConnectionProps);
            Class.forName("com.amazonaws.secretsmanager.sql.AWSSecretsManagerPostgreSQLDriver").newInstance();
            connection = DriverManager.getConnection(dbUrl, username, password);
        } catch (IOException e) {
            throw new IllegalStateException("Database secret cannot be parsed!");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("Database connection could not be established", e);
        }
    }

    private String getDbUrlFrom(Map<String, Object> dbConnectionProperties) {
        final var rawHost = dbConnectionProperties.get("host");
        Objects.requireNonNull(rawHost, "Database secret does not contain host!");
        final var host = String.valueOf(rawHost);

        final Object rawPort = dbConnectionProperties.get("port");
        Objects.requireNonNull(rawPort, "Database secret does not contain port!");
        final var port = String.valueOf(rawPort);

        final Object rawDbName = dbConnectionProperties.get("engine");
        Objects.requireNonNull(rawPort, "Database secret does not contain engine!");
        final var dbName = String.valueOf(rawDbName);

        return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
    }

    private String getUsernameFrom(Map<String, Object> dbConnectionProperties) {
        final var rawUsername = dbConnectionProperties.get("username");
        Objects.requireNonNull(rawUsername, "Database secret does not contain username!");
        return String.valueOf(rawUsername);
    }

    private String getPasswordFrom(Map<String, Object> dbConnectionProperties) {
        final var rawPassword = dbConnectionProperties.get("password");
        Objects.requireNonNull(rawPassword, "Database secret does not contain password!");
        return String.valueOf(rawPassword);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
