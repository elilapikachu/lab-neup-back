package com.neup.web.integration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.neup.web.utils.ConfigurationReader;
import lombok.extern.slf4j.Slf4j;

import static com.neup.web.utils.ConstantesEntorno.NAME_DB;
import static com.neup.web.utils.ConstantesEntorno.URL_DB;

@Slf4j
public class ConnectionFactory {
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    private ConnectionFactory() {
        // Constructor privado para evitar instanciación
    }

    public static synchronized MongoClient getMongoClient() {
        if (mongoClient == null) {
            try {
                String mongoUri = ConfigurationReader.getProperty(URL_DB);
                log.info("Conectando a MongoDB: {}", mongoUri);
                mongoClient = MongoClients.create(mongoUri);
                log.info("Conexión a MongoDB establecida exitosamente");

            } catch (Exception e) {
                log.error("Error al conectar a MongoDB", e);
                throw new RuntimeException("No se pudo conectar a MongoDB", e);
            }
        }
        return mongoClient;
    }

    public static synchronized MongoDatabase getDatabase() {
        if (mongoDatabase == null) {
            try {
                String databaseName = ConfigurationReader.getProperty(NAME_DB);

                mongoDatabase = getMongoClient().getDatabase(databaseName);
                log.info("Base de datos seleccionada: {}", databaseName);

            } catch (Exception e) {
                log.error("Error al obtener la base de datos", e);
                throw new RuntimeException("No se pudo obtener la base de datos", e);
            }
        }
        return mongoDatabase;
    }

    public static synchronized void closeConnection() {
        if (mongoClient != null) {
            try {
                mongoClient.close();
                mongoClient = null;
                mongoDatabase = null;
                log.info("Conexión a MongoDB cerrada");
            } catch (Exception e) {
                log.error("Error al cerrar la conexión a MongoDB", e);
            }
        }
    }

    public static boolean isConnected() {
        try {
            getMongoClient().getDatabase("admin").runCommand(new org.bson.Document("ping", 1));
            return true;
        } catch (Exception e) {
            log.warn("La conexión a MongoDB no está disponible", e);
            return false;
        }
    }
}
