package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.model.Persona;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonaRepository {


    private static final String COLECCION = "persona";

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(COLECCION);
    }

    public Optional<Document> findByUsuarioId(String usuarioId) {
        Document filtro = new Document("usuario_id", new org.bson.types.ObjectId(usuarioId));
        Document resultado = getColeccion().find(filtro).first();
        return Optional.ofNullable(resultado);
    }

    public Optional<Document> findById(String id) {
        Document filtro = new Document("_id", new org.bson.types.ObjectId(id));
        Document resultado = getColeccion().find(filtro).first();
        return Optional.ofNullable(resultado);
    }
    public ObjectId insertar(Persona persona) {
        Document contactos = new Document()
                .append("telefono", persona.getContactos() != null ? persona.getContactos().getTelefono() : null)
                .append("otro_email", persona.getContactos() != null ? persona.getContactos().getOtroEmail() : null);

        Document caracteristicas = new Document()
                .append("peso", persona.getCaracteristicasFisicas() != null ? persona.getCaracteristicasFisicas().getPeso() : null)
                .append("altura", persona.getCaracteristicasFisicas() != null ? persona.getCaracteristicasFisicas().getAltura() : null);

        Document preferencias = new Document()
                .append("gustos", List.of())
                .append("alergias", List.of())
                .append("tipo_dieta", List.of())
                .append("objetivos", List.of());

        Document dietas = new Document()
                .append("guardadas", List.of())
                .append("personalizadas", List.of());

        Document recetas = new Document()
                .append("guardadas", List.of())
                .append("personalizadas", List.of());

        Document doc = new Document()
                .append("nombres", persona.getNombres())
                .append("apellidos", persona.getApellidos())
                .append("contactos", contactos)
                .append("caracteristicas_fisicas", caracteristicas)
                .append("preferencias", preferencias)
                .append("usuario_id", persona.getUsuarioId())
                .append("dietas", dietas)
                .append("recetas", recetas);

        getColeccion().insertOne(doc);
        return doc.getObjectId("_id");
    }

    public boolean actualizar(String id, Document campos) {
        Document filtro = new Document("_id", new ObjectId(id));
        Document update = new Document("$set", campos);
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean eliminar(String id) {
        Document filtro = new Document("_id", new ObjectId(id));
        return getColeccion().deleteOne(filtro).getDeletedCount() > 0;
    }
}
