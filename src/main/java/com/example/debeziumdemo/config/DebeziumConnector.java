package com.example.debeziumdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.debezium.config.Configuration;

@org.springframework.context.annotation.Configuration
public class DebeziumConnector {

    @Value("${server.source-db-host}")
    private String sourceDbHost;

    @Value("${server.source-db-port}")
    private String sourceDbPort;

    @Value("${server.source-db-user}")
    private String sourceDbUser;

    @Value("${server.source-db-password}")
    private String sourceDbPassword;

    @Value("${server.source-db-name}")
    private String sourceDbName;

    @Bean
    public Configuration ordersConnector() {
        return Configuration.create()
            .with("name", "order-connector")
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with("offset.storage.file.filename", "/tmp/offsets.dat")
            .with("offset.flush.interval.ms", "60000")
            .with("database.hostname", sourceDbHost)
            .with("database.port", sourceDbPort)
            .with("database.user", sourceDbUser)
            .with("database.password", sourceDbPassword)
            .with("database.dbname", sourceDbName)
            .with("database.include.list", sourceDbName)
            .with("include.schema.changes", "false")
//            .with("database.server.id", "10181")
//            .with("database.server.name", "source-postgres-db-server")
            .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
            .with("database.history.file.filename", "/tmp/dbhistory.dat")
            .build();
    }

}
