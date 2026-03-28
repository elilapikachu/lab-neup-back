package com.neup.web.integration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBRepository {
    private static final Logger logger = LoggerFactory.getLogger(DBRepository.class);
    private final MongoCollection<Document> collection;
    @Getter
    private final String collectionName;

    public DBRepository(MongoDatabase database, String collectionName) {
        this.collectionName = collectionName;
        this.collection = database.getCollection(collectionName);
        logger.info("Repositorio inicializado para la colección: {}", collectionName);
    }

    public void insert(Map<String, Object> document) {
        try {
            collection.insertOne(new Document(document));
            logger.debug("Documento insertado en {}", collectionName);
        } catch (Exception e) {
            logger.error("Error al insertar documento en {}", collectionName, e);
            throw new RuntimeException("Error al insertar en " + collectionName, e);
        }
    }

    public void insertMany(List<Map<String, Object>> documents) {
        try {
            List<Document> docList = new ArrayList<>();
            for (Map<String, Object> doc : documents) {
                docList.add(new Document(doc));
            }
            collection.insertMany(docList);
            logger.debug("{} documentos insertados en {}", docList.size(), collectionName);
        } catch (Exception e) {
            logger.error("Error al insertar múltiples documentos en {}", collectionName, e);
            throw new RuntimeException("Error al insertar en " + collectionName, e);
        }
    }

    public Document findOne(Map<String, Object> filter) {
        try {
            Bson bsonFilter = createFilter(filter);
            return collection.find(bsonFilter).first();
        } catch (Exception e) {
            logger.error("Error al buscar documento en {}", collectionName, e);
            throw new RuntimeException("Error al buscar en " + collectionName, e);
        }
    }

    public List<Document> findMany(Map<String, Object> filter) {
        try {
            Bson bsonFilter = createFilter(filter);
            return collection.find(bsonFilter).into(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error al buscar documentos en {}", collectionName, e);
            throw new RuntimeException("Error al buscar en " + collectionName, e);
        }
    }

    public List<Document> findAll() {
        try {
            return collection.find().into(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error al obtener todos los documentos de {}", collectionName, e);
            throw new RuntimeException("Error al obtener documentos de " + collectionName, e);
        }
    }

    public UpdateResult updateOne(Map<String, Object> filter, Map<String, Object> updates) {
        try {
            Bson bsonFilter = createFilter(filter);
            Document updateDoc = new Document("$set", new Document(updates));
            UpdateResult result = collection.updateOne(bsonFilter, updateDoc);
            logger.debug("Documento actualizado en {} - Documentos modificados: {}", collectionName, result.getModifiedCount());
            return result;
        } catch (Exception e) {
            logger.error("Error al actualizar documento en {}", collectionName, e);
            throw new RuntimeException("Error al actualizar en " + collectionName, e);
        }
    }

    public UpdateResult updateMany(Map<String, Object> filter, Map<String, Object> updates) {
        try {
            Bson bsonFilter = createFilter(filter);
            Document updateDoc = new Document("$set", new Document(updates));
            UpdateResult result = collection.updateMany(bsonFilter, updateDoc);
            logger.debug("{} documentos actualizados en {}", result.getModifiedCount(), collectionName);
            return result;
        } catch (Exception e) {
            logger.error("Error al actualizar múltiples documentos en {}", collectionName, e);
            throw new RuntimeException("Error al actualizar en " + collectionName, e);
        }
    }

    public DeleteResult deleteOne(Map<String, Object> filter) {
        try {
            Bson bsonFilter = createFilter(filter);
            DeleteResult result = collection.deleteOne(bsonFilter);
            logger.debug("Documento eliminado de {} - Documentos removidos: {}", collectionName, result.getDeletedCount());
            return result;
        } catch (Exception e) {
            logger.error("Error al eliminar documento de {}", collectionName, e);
            throw new RuntimeException("Error al eliminar de " + collectionName, e);
        }
    }

    public DeleteResult deleteMany(Map<String, Object> filter) {
        try {
            Bson bsonFilter = createFilter(filter);
            DeleteResult result = collection.deleteMany(bsonFilter);
            logger.debug("{} documentos eliminados de {}", result.getDeletedCount(), collectionName);
            return result;
        } catch (Exception e) {
            logger.error("Error al eliminar múltiples documentos de {}", collectionName, e);
            throw new RuntimeException("Error al eliminar de " + collectionName, e);
        }
    }

    private Bson createFilter(Map<String, Object> filter) {
        if (filter == null || filter.isEmpty()) {
            return new Document();
        }
        return Filters.and(
                filter.entrySet().stream()
                        .map(entry -> Filters.eq(entry.getKey(), entry.getValue()))
                        .toArray(Bson[]::new)
        );
    }

    public long count(Map<String, Object> filter) {
        try {
            Bson bsonFilter = createFilter(filter);
            return collection.countDocuments(bsonFilter);
        } catch (Exception e) {
            logger.error("Error al contar documentos en {}", collectionName, e);
            throw new RuntimeException("Error al contar en " + collectionName, e);
        }
    }
}