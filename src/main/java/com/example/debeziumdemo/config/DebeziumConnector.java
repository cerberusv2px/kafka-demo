package com.example.debeziumdemo.config;

import java.io.File;
import java.io.IOException;

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
    public Configuration ordersConnector() throws IOException {

        File offsetStorageTempFile = File.createTempFile("offsets_", ".dat");
        File dbHistoryTempFile = File.createTempFile("dbhistory_", ".dat");
        return io.debezium.config.Configuration.create()
            .with("name", "order-connector")
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
            .with("offset.flush.interval.ms", "60000")
            .with("database.hostname", sourceDbHost)
            .with("database.port", sourceDbPort)
            .with("database.user", sourceDbUser)
            .with("database.password", sourceDbPassword)
            .with("database.dbname", sourceDbName)
            //.with("database.include.list", sourceDbName)
            .with("include.schema.changes", "false")
            .with("database.allowPublicKeyRetrieval", "true")
            .with("database.server.id", "10181")
            .with("database.server.name", "source-postgres-db-server")
            .with("table.whitelist", "public.orders")
            .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
            .with("database.history.file.filename", dbHistoryTempFile.getAbsolutePath())
            .build();
    }

}
